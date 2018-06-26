echo "Building $1"
LOCAL_VARIABLE="$1" && echo $(cd $(dirname "$LOCAL_VARIABLE") && pwd -P)/$(basename "$LOCAL_VARIABLE")
echo $LOCAL_VARIABLE
java -jar ./build/libs/KotlinSharp-0.1.jar $LOCAL_VARIABLE
wait
ilasm /output:$1.exe $1.il
mono $1.exe