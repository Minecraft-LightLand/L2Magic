package test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class CodecTest {

	public static final Codec<CodecTest> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.either(Codec.STRING, Codec.list(Codec.STRING)).fieldOf("data").forGetter(e -> Either.right(e.data))
	).apply(i, CodecTest::new));

	private final List<String> data;

	public CodecTest(Either<String, List<String>> data) {
		this.data = data.map(List::of, e -> e);
	}

	public static void main(String[] args) {
		JsonObject obj_0 = new JsonObject();
		obj_0.addProperty("data", "abc");
		CodecTest res_0 = CODEC.decode(JsonOps.INSTANCE, obj_0).result().get().getFirst();
		System.out.println(res_0.data);
		JsonObject obj_1 = new JsonObject();
		JsonArray arr = new JsonArray();
		arr.add("123");
		arr.add("456");
		obj_1.add("data", arr);
		CodecTest res_1 = CODEC.decode(JsonOps.INSTANCE, obj_1).result().get().getFirst();
		System.out.println(res_1.data);
	}

}
