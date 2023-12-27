package kosta.main.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TimeTraceAspect {

    // @Pointcut("execution(* com.example.demo.repository..*(..))")
    @Pointcut("@annotation(kosta.main.global.aop.TimeTrace)")
    private void timeTracePointcut() {
    }

    @Around("timeTracePointcut()")
    public void traceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        joinPoint.proceed(); // 조인포인트의 메서드 실행
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getStopTime() - stopWatch.getStartTime() ;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis);

    }
}