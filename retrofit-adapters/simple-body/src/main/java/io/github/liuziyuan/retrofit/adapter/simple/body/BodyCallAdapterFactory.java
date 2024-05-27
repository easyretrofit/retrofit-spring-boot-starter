package io.github.liuziyuan.retrofit.adapter.simple.body;

import okhttp3.ResponseBody;
import retrofit2.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Retrofit2 Sync Call Adapter, Response a ResponseBody
 */
public class BodyCallAdapterFactory extends CallAdapter.Factory {

    private final Class<?>[] excludeCallTypes;

    private final Function<ResponseBody, ?> function;

    public BodyCallAdapterFactory(Class<?>[] excludeCallTypes, Function<ResponseBody, ?> function) {
        this.excludeCallTypes = excludeCallTypes;
        this.function = function;
    }

    /**
     * create a BodyCallAdapterFactory <br>
     * When returnType is retrofit2 official call adapter type , return null
     *
     * @return BodyCallAdapterFactory
     */
    public static BodyCallAdapterFactory create() {
        return new BodyCallAdapterFactory(null, null);
    }

    /**
     * create a BodyCallAdapterFactory <br>
     *
     * @param exclude Manually exclude call adapter type
     * @return BodyCallAdapterFactory
     */
    public static BodyCallAdapterFactory create(Class<?>[] exclude) {
        return new BodyCallAdapterFactory(exclude, null);
    }

    public static BodyCallAdapterFactory create(Class<?>[] exclude, Function<ResponseBody, ?> function) {
        return new BodyCallAdapterFactory(exclude, function);
    }

    public static BodyCallAdapterFactory create(Function<ResponseBody, ?> function) {
        return new BodyCallAdapterFactory(null, function);
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        Class<?> rawType = getRawType(returnType);
        String typeName = rawType.getName();
        if (Call.class.isAssignableFrom(rawType)) {
            return null;
        }
        if (Response.class.isAssignableFrom(rawType)) {
            return null;
        }
        if (CompletableFuture.class.isAssignableFrom(rawType)) {
            return null;
        }
        if (excludeCallTypes != null) {
            for (Class<?> callType : excludeCallTypes) {
                if (callType == rawType) {
                    return null;
                }
            }
        }
        // if retrofit official async adapters, return null
        if ("io.reactivex.rxjava3.core.Observable".equals(typeName) || "io.reactivex.rxjava3.core.Single".equals(typeName) || "io.reactivex.rxjava3.core.Completable".equals(typeName) || "io.reactivex.rxjava3.core.Flowable".equals(typeName) || "io.reactivex.rxjava3.core.Maybe".equals(typeName)) {
            return null;
        }
        if ("io.reactivex.Observable".equals(typeName) || "io.reactivex.Single".equals(typeName) || "io.reactivex.Completable".equals(typeName) || "io.reactivex.Flowable".equals(typeName) || "io.reactivex.Maybe".equals(typeName)) {
            return null;
        }
        if ("rx.Observable".equals(typeName) || "rx.Single".equals(typeName) || "rx.Completable".equals(typeName)) {
            return null;
        }
        if ("com.google.common.util.concurrent.ListenableFuture".equals(typeName)) {
            return null;
        }
        if ("reactor.core.publisher.Mono".equals(typeName) || "reactor.core.publisher.Flux".equals(typeName)) {
            return null;
        }

        return new BodyCallAdapter<>(rawType, annotations, retrofit, function);
    }

    static final class BodyCallAdapter<R> implements CallAdapter<R, R> {

        private final Type returnType;

        private final Retrofit retrofit;

        private final Annotation[] annotations;

        private final Function<ResponseBody,?> function;

        BodyCallAdapter(Type returnType, Annotation[] annotations, Retrofit retrofit, Function<ResponseBody, ?> function) {
            this.returnType = returnType;
            this.retrofit = retrofit;
            this.annotations = annotations;
            this.function = function;
        }

        @Override
        public Type responseType() {
            return returnType;
        }

        @Override
        public R adapt(Call<R> call) {
            Response<R> response;
            try {
                response = call.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (response.isSuccessful()) {
                return response.body();
            }

            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                return null;
            }
            if (function != null) {
                Object apply = function.apply(errorBody);
                return (R) apply;
            }
            Converter<ResponseBody, R> converter = retrofit.responseBodyConverter(responseType(), annotations);
            try {
                return converter.convert(Objects.requireNonNull(errorBody));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
