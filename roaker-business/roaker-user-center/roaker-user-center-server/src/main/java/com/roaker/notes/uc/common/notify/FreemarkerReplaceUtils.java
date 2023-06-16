package com.roaker.notes.uc.common.notify;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author lei.rao
 * @since 1.0
 */
public class FreemarkerReplaceUtils {

    static Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    static {
        cfg.setTemplateLoader(new StringTemplateLoader());
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
    }

    @SneakyThrows
    public static String wrapAndReplace(String content, Map<String, Object> placeHolderParams) {
        Template template = new Template("template", content, cfg);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, placeHolderParams);
    }
}
