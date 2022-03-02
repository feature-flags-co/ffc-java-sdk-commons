# java-sdk-commons

This project is a commons library for users to use/serialize/deserialize ffc objects

`FFUser`: A collection of attributes that can affect flag evaluation, usually corresponding to a user of your application.
This object contains built-in properties(`key`, `userName`, `email` and `country`). The only mandatory property is the key, 
which must uniquely identify each user; this could be a username or email address for authenticated users, or a ID for anonymous users. 
All other built-in properties are optional, it's strongly recommended to set userName in order to search your user quickly
You may also define custom properties with arbitrary names and values.

```()
     FFCClient user = new FFCClient.Builder("key")
        .userName("name")
        .country("country")
        .email("email@xxx.com")
        .custom("property", "value")
        .build()
```

`EvalDetail`: An object combines the result of a flag evaluation with an explanation of how it was calculated.
This object contains a general type of variation value, that could be string, boolean or number; a reason to explain 
how it was calculated; an id within the flag's list of variations, whose value is -1, it means that the default value returned,
it could have an error in the last evaluation; finally both flag name and flag key name. 

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

`FlagState` and `AllFlagStates` the 2 objects encapsulate the EvalDetail, which contains the details of evaluation of
feature flag. Java Server SDK uses them to return the evaluation of a given feature flag.
```
FlagState<Boolean> flagState = FlagState.of(res);
json = flagState.jsonfy();
System.out.println(json);
flagState = flagState.fromJson(json, Boolean.class);
System.out.println(res);

AllFlagStates<Boolean> allFlagStates = AllFlagStates.of(true, null, Arrays.asList(res));
json = allFlagStates.jsonfy();
System.out.println(json);
allFlagStates = AllFlagStates.fromJson(json, Boolean.class);
System.out.println(res);
```