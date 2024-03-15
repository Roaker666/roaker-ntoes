package com.roaker.notes.flink.learning;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author lei.rao
 * @since 1.0
 */
public class IterativeStreamTest {
    public static void main(String[] args) throws Exception {
        String host = args[0];
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //docker "docker.for.mac.localhost"
        DataStreamSource<String> dataStreamSource = env.socketTextStream(host, 8888);
        IterativeStream<Long> iterativeStream = dataStreamSource.map(Long::parseLong)
                .iterate();
        DataStream<Long> zero = iterativeStream.map(IterativeStreamTest::lessThanMaxFib);
        //获取执行的流,对于大于0的数据都会被重新计算
        SingleOutputStreamOperator<Long> stillGreaterThanZero = zero.filter(ele -> ele > 0);
        iterativeStream.closeWith(stillGreaterThanZero);
        SingleOutputStreamOperator<Long> lessThanZero = zero.filter(ee -> ee <= 0);
        zero.print("IterationDemo");
        env.execute();

    }

    public static Long lessThanMaxFib(Long i) {
        long f0 = 0L, f1= 1L;
        while (f1 < i) {
            long t = f1;
            f1 = f0 + f1;
            f0= t;
        }
        return f0;
    }
}
