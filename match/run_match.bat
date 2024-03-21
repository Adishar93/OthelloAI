:: 
mkdir build
cd build
rm *
cd ..
cd Bot1
cp OthelloAI.java ../build
cd ../Bot2
cp OthelloAI2.java ../build
cd ../dependencies
cp * ../build
cd ..
cp Refree.java ./build
cd build
javac *
PAUSE
java Refree
PAUSE
