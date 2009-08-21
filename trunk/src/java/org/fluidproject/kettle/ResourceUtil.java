/*
 * Created on 20 Jun 2009
 */
package org.fluidproject.kettle;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.json.support.DeJSONalizer;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.streamutil.StreamCloseUtil;
import uk.org.ponder.util.UniversalRuntimeException;

public class ResourceUtil {

    static Object readJson(String filename, Object root) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            DeJSONalizer de = new DeJSONalizer(SAXalizerMappingContext.instance(), fis);
            Object togo = de.readObject(root, null);
            if (togo.getClass() != root.getClass()) {
                throw new IOException("Error in JSON file - expected root of " + 
                        root.getClass() + " but received " + togo.getClass());
            }
            return togo;
        }
        catch (Exception e) {
            throw UniversalRuntimeException.accumulate(e, 
                    "Error reading JSON file " + filename + " - invalid format");
        }
        finally {
            
        }
    }
    
    public static String[] loadJsonArray(String filename) {
        return (String[]) readJson(filename, new String[] {});
    }

    public static Map loadJson(String filename) {
        return (Map) readJson(filename, new HashMap());
    }

    public static void sendResponse(HttpServletResponse response, Object tosend) {
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            String s = tosend.toString();
            os.write(s.getBytes("UTF-8"));
        }
        catch (Exception e) {
            System.err.print("Error writing response: " + e.getMessage());
        }
        finally {
            StreamCloseUtil.closeOutputStream(os);
        }
    }
    
}