package ru.osipov.deploy.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import ru.osipov.deploy.errors.ApiException;
import ru.osipov.deploy.models.GenreInfo;
import ru.osipov.deploy.services.WebGenreService;

import java.net.ConnectException;
import java.util.List;

public class GenreGetAllCommand extends HystrixCommand<GenreInfo[]> {

    private WebGenreService service;
    private boolean failSilently;

    public GenreGetAllCommand(WebGenreService service, boolean failSilently) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GenreServiceGroup")).andThreadPoolKey(
                HystrixThreadPoolKey.Factory.asKey("GenreServicePool")));
        this.service = service;
        this.failSilently = failSilently;
    }


    @Override
    protected GenreInfo[] run() throws Exception {
        return service.getAll();
    }

    @Override
    protected GenreInfo[] getFallback() {
        if (failSilently) {
            return new GenreInfo[0];
        } else {
            throw new ApiException("Service unavailable.",new ConnectException(),500,null,"Service unavailable!!! Connection refused.","/v1/genres",null);
        }
    }
}
