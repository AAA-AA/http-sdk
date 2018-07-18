package com.github.http.sdk.exception;

import github.com.http.sdk.utils.Clean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : hongqiangren.
 * @since: 2018/6/11 23:22
 */
public class HttpException extends RuntimeException {

    static final Pattern split = Pattern.compile("\\{\\s*\\}");

    private int code;
    private String msg;

    public HttpException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public HttpException(String message, Object... args) {
        super(format(message,args));
    }

    static String format(String formatter, Object... args) {
        if (null == args || args.length < 1 || null == formatter || !split.matcher(formatter).find()) {
            return Clean.pick(() -> formatter, () -> "");
        }
        StringBuilder bu = new StringBuilder();
        String[] cuts = formatter.split(split.pattern());
        for (int index = 0; index < cuts.length; index++) {
            bu.append(cuts[index]);
            if (index < args.length) {
                bu.append(args[index]);
            } else if (index < cuts.length - 1) {
                // bu.append("{}");
            }
        }
        return bu.toString();
    }

    public static void main(String[] args) {
        String test = "this is test %s {} {} {}";

        String result = format(test,1,2,"demo");

        System.out.println(result);
        String regx = "\\$\\{[a-zA-Z]*\\}";
        Pattern pattern = Pattern.compile(regx);

        String testStr2 = "wo huo yi , ${matter}, ${dies}";
        Matcher matcher = pattern.matcher(testStr2);
        while (matcher.find()) {
            String group = matcher.group().replaceAll("\\$\\{","").replaceAll("\\}","");
            System.out.println(group);
        }

    }

    public HttpException(String message) {
        super(message);
        this.msg = message;
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }
}
