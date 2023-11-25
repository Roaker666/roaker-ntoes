package com.roaker.notes.flink.learning;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author lei.rao
 * @since 1.0
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //监听宿主机这样写
        DataStreamSource<String> dataStream = env.socketTextStream("docker.for.mac.localhost", 8888);
        dataStream.setParallelism(1);
        dataStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                        for (String s1 : s.split(" ")) {
                            collector.collect(new Tuple2<>(s1, 1));
                        }
                    }
                }).keyBy(0)
                .sum(1)
                .print();
        env.execute();

    }
}
