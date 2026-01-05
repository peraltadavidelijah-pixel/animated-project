echo "compile"
javac -cp ".:lib/animation-2025-07-09.jar" animations/*.java backgrounds/*.java gui/*.java main/*.java sprites/*.java universes/*.java backgrounds/*.java  -d ./bin

echo "run Examples.java"
java -classpath ./bin:lib/animation-2025-07-09.jar Examples
