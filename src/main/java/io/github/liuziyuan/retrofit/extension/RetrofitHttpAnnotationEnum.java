package io.github.liuziyuan.retrofit.extension;

import retrofit2.http.*;

import java.lang.annotation.Annotation;

/**
 * @author liuziyuan
 */
public enum RetrofitHttpAnnotationEnum {
    HTTP_GET(GET.class), HTTP_POST(POST.class), HTTP_PUT(PUT.class), HTTP_DELETE(DELETE.class), HTTP_PATCH(PATCH.class), HTTP_OPTIONS(OPTIONS.class);

    private Class<? extends Annotation> clazz;

    RetrofitHttpAnnotationEnum(Class<? extends Annotation> clazz) {
        this.clazz = clazz;
    }


    public Class<? extends Annotation> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Annotation> clazz) {
        this.clazz = clazz;
    }
}
