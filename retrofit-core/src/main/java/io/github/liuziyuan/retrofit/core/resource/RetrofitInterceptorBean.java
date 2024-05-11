package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.annotation.InterceptorType;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptorParam;
import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@ToString
public class RetrofitInterceptorBean {
    private Class<? extends BaseInterceptor> handler;

    private InterceptorType type;

    private String[] include;

    private String[] exclude;

    private int sort;

    public RetrofitInterceptorBean(RetrofitInterceptor retrofitInterceptor, RetrofitInterceptorParam retrofitInterceptorParam) {
        this.handler = retrofitInterceptor.handler();
        this.type = retrofitInterceptorParam.type();
        this.include = retrofitInterceptorParam.include();
        this.exclude = retrofitInterceptorParam.exclude();
        this.sort = retrofitInterceptorParam.sort();
    }

    public RetrofitInterceptorBean(RetrofitInterceptor retrofitInterceptor) {
        this.handler = retrofitInterceptor.handler();
        this.type = retrofitInterceptor.type();
        this.include = retrofitInterceptor.include();
        this.exclude = retrofitInterceptor.exclude();
        this.sort = retrofitInterceptor.sort();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RetrofitInterceptorBean that = (RetrofitInterceptorBean) o;

        // Compare the fields' toString() values
        return Objects.equals(this.handler.toString(), that.handler.toString())
                && Objects.equals(this.type.toString(), that.type.toString())
                && Arrays.equals(this.include, that.include)
                && Arrays.equals(this.exclude, that.exclude)
                && this.sort == that.sort;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(handler.toString(), type.toString());
        result = 31 * result + Arrays.hashCode(include);
        result = 31 * result + Arrays.hashCode(exclude);
        result = 31 * result + sort;
        return result;
    }

}
