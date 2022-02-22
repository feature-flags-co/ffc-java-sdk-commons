# java-sdk-commons

This project is a commons library for users to use/serialize/deserialize ffc objects

`FFUser`: A collection of attributes that can affect flag evaluation, usually corresponding to a user of your application.

```
FFCClient user = new FFCClient.Builder("key")
                   .userName("name")
                   .country("CN")
                   .build()
```

`EvalDetail`: An object returned by the SDK's "variation detail" methods such as boolVariationDetail, combining the result of a flag evaluation with an explanation of how it was calculated.
```
EvalDetail<Boolean> res = EvalDetail.of(true, 1, "test", "key1", "flag1");
String json = res.jsonfy();
System.out.println(json);
res = EvalDetail.fromJson(json, Boolean.class);
System.out.println(res);
```

`VariationParams`: an object is used to pass the FFClient and flag key name to Server SDK Wrapped API

```
VariationParams params = VariationParams.of("PayButton", user);
String json = params.jsonfy();
System.out.println(json);
params = VariationParams.fromJson(json);
System.out.println(params);
```
