package commitmessagetemplate.util;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.SimpleLog4JLogSystem;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by Arsen on 15.03.2017.
 */
public class VelocityHelper {

//    public static String fromTemplate(String velocityTemplate, Map<String, String> mapVariables) {
//        StringWriter stringWriter = new StringWriter();
//        try {
//            VelocityEngine velocityEngine = getVelocityEngine();
//
//            VelocityContext velocityContext = new VelocityContext();
//            for (Map.Entry<String, String> entry : mapVariables.entrySet()) {
//                velocityContext.put(entry.getKey(), entry.getValue());
//            }
//
//            velocityEngine.evaluate(velocityContext, stringWriter, "CommitMessageTemplateVelocity", velocityTemplate);
//        } catch (Exception e) {
//            Logger.log("fromTemplate Error in Velocity code generator");
//            return null;
//        }
//        return stringWriter.getBuffer().toString();
//    }

    public static String fromTemplate(String velocityTemplate, Map<String, Object> mapVariables) {
        StringWriter stringWriter = new StringWriter();
        try {
            VelocityEngine velocityEngine = getVelocityEngine();

            VelocityContext velocityContext = new VelocityContext();
            for (Map.Entry<String, Object> entry : mapVariables.entrySet()) {
                velocityContext.put(entry.getKey(), entry.getValue());
            }

            velocityEngine.evaluate(velocityContext, stringWriter, "CommitMessageTemplateVelocity", velocityTemplate);
        } catch (Exception e) {
            Logger.log("fromTemplate Error in Velocity code generator");
            return null;
        }
        return stringWriter.getBuffer().toString();
    }

    public static String fromTemplate(String velocityTemplate, String key, Object obj) {
        StringWriter stringWriter = new StringWriter();
        try {
            VelocityEngine velocityEngine = getVelocityEngine();

            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put(key, obj);

            velocityEngine.evaluate(velocityContext, stringWriter, "CommitMessageTemplateVelocity", velocityTemplate);
        } catch (Exception e) {
            Logger.log("fromTemplate Error in Velocity code generator");
            return null;
        }
        return stringWriter.getBuffer().toString();
    }


    //=================================================================
    //  Engine
    //=================================================================
    private static class Holder {
        private static final VelocityEngine engine = newVelocityEngine();
    }

    private static VelocityEngine newVelocityEngine() {
        ExtendedProperties prop = new ExtendedProperties();
        prop.addProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, SimpleLog4JLogSystem.class.getName());
        prop.addProperty("runtime.log.logsystem.log4j.category", "GenerateToString");

        VelocityEngine velocity = new VelocityEngine();
        velocity.setExtendedProperties(prop);
        velocity.init();

        return velocity;
    }

    private static VelocityEngine getVelocityEngine() {
        return Holder.engine;
    }

}
