
package org.apache.flink.playgrounds.ops.clickcount;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.playgrounds.ops.clickcount.records.TaxiRide;
import org.apache.flink.playgrounds.ops.clickcount.records.TaxiRideEventDeserializationSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DeliveryEventCount {
	public static final String CHECKPOINTING_OPTION = "checkpointing";
	public static final String EVENT_TIME_OPTION = "event-time";
	public static final String OPERATOR_CHAINING_OPTION = "chaining";

	public static void main(String[] args) throws Exception {
		final ParameterTool params = ParameterTool.fromArgs(args);
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		configureEnvironment(params, env);

		String inputTopic = params.get("input-topic", "input");
		String brokers = params.get("bootstrap.servers", "localhost:9092");
		Properties kafkaProps = new Properties();
		kafkaProps.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
		kafkaProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "click-event-count");

		DataStream<TaxiRide> clicks =
				env.addSource(new FlinkKafkaConsumer<>(inputTopic, new TaxiRideEventDeserializationSchema(), kafkaProps))
			.name("DeliveryEvent Source");

		DataStream<String> statistics = clicks
				.map(new MapFunction<TaxiRide, String>() {
					@Override
					public String map(TaxiRide ride) {
						return  ride.toString();
					}})
				.name("DeliveryEvent Converter");

		final FileSink<String> sink = FileSink
				.forRowFormat(new Path("/tmp/flink-output"), new SimpleStringEncoder<String>("UTF-8"))
				.withRollingPolicy(
						DefaultRollingPolicy.builder()
								.withRolloverInterval(TimeUnit.SECONDS.toMillis(15))
								.withInactivityInterval(TimeUnit.SECONDS.toMillis(5))
								.withMaxPartSize(128 * 128 * 128)
								.build())
				.build();

		statistics.sinkTo(sink);

		env.execute("Delivery Event Saving");
	}

	private static void configureEnvironment(
			final ParameterTool params,
			final StreamExecutionEnvironment env) {

		boolean checkpointingEnabled = params.has(CHECKPOINTING_OPTION);
		boolean eventTimeSemantics = params.has(EVENT_TIME_OPTION);
		boolean enableChaining = params.has(OPERATOR_CHAINING_OPTION);

		if (checkpointingEnabled) {
			env.enableCheckpointing(1000);
		}

		if (eventTimeSemantics) {
			env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		}

		if(!enableChaining){
			//disabling Operator chaining to make it easier to follow the Job in the WebUI
			env.disableOperatorChaining();
		}
	}
}
