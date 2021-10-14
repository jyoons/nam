package com.namandnam.nni.common.vo;

import java.io.Serializable;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * 파 일 명 : BeanError.java
 * 설    명 : 빈 에러 정보
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BeanError implements Serializable {

    private String code;
    private String field;
    private String message;
    private String[] extra;

    @Builder
    public BeanError(String code, String field, String message, String[] extra) {
        this.code = code;
        this.field = field;
        this.message = message;
        this.extra = extra != null ? Arrays.copyOf(extra, extra.length) : null;
    }
}
