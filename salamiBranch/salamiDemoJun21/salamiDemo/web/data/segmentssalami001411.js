var data = [
[{o: 0.325, f: 25.334, l: "A", a: 0},
{o: 25.334, f: 96.758, l: "B", a: 1},
{o: 96.758, f: 167.904, l: "C", a: 0},
{o: 167.904, f: 214.027, l: "C'", a: 1},
{o: 214.027, f: 274.343, l: "E", a: 0},
{o: 274.343, f: 286.529, l: "F", a: 1}],
[{o: 0.184, f: 11.636, l: "D", a: 0},
{o: 11.636, f: 24.384, l: "D", a: 1},
{o: 24.384, f: 33.54, l: "A", a: 0},
{o: 33.54, f: 51.82, l: "A", a: 1},
{o: 51.82, f: 63.432, l: "A", a: 0},
{o: 63.432, f: 79.184, l: "A", a: 1},
{o: 79.184, f: 91.748, l: "A", a: 0},
{o: 91.748, f: 98.924, l: "A", a: 1},
{o: 98.924, f: 121.792, l: "A", a: 0},
{o: 121.792, f: 138.08, l: "A", a: 1},
{o: 138.08, f: 161.868, l: "A", a: 0},
{o: 161.868, f: 174.82, l: "A", a: 1},
{o: 174.82, f: 183.78, l: "A", a: 0},
{o: 183.78, f: 204.864, l: "A", a: 1},
{o: 204.864, f: 215.536, l: "B", a: 0},
{o: 215.536, f: 228.524, l: "C", a: 1},
{o: 228.524, f: 244.952, l: "A", a: 0},
{o: 244.952, f: 263.4, l: "A", a: 1},
{o: 263.4, f: 278.92, l: "B", a: 0}],
[{o: 0.184, f: 11.636, l: "D", a: 0},
{o: 11.636, f: 24.384, l: "D", a: 1},
{o: 24.384, f: 33.54, l: "A", a: 0},
{o: 33.54, f: 51.82, l: "A", a: 1},
{o: 51.82, f: 63.432, l: "C", a: 0},
{o: 63.432, f: 79.184, l: "F", a: 1},
{o: 79.184, f: 91.748, l: "E", a: 0},
{o: 91.748, f: 98.924, l: "G", a: 1},
{o: 98.924, f: 121.792, l: "H", a: 0},
{o: 121.792, f: 138.08, l: "C", a: 1},
{o: 138.08, f: 161.868, l: "A", a: 0},
{o: 161.868, f: 174.82, l: "E", a: 1},
{o: 174.82, f: 183.78, l: "I", a: 0},
{o: 183.78, f: 204.864, l: "C", a: 1},
{o: 204.864, f: 215.536, l: "B", a: 0},
{o: 215.536, f: 228.524, l: "J", a: 1},
{o: 228.524, f: 244.952, l: "A", a: 0},
{o: 244.952, f: 263.4, l: "A", a: 1},
{o: 263.4, f: 278.92, l: "B", a: 0}],
[{o: 0.52, f: 5.147, l: "5", a: 0},
{o: 5.147, f: 33.16, l: "1", a: 1},
{o: 33.16, f: 43.787, l: "2", a: 0},
{o: 43.787, f: 65.867, l: "1", a: 1},
{o: 65.867, f: 73.907, l: "5", a: 0},
{o: 73.907, f: 90.707, l: "1", a: 1},
{o: 90.707, f: 99.48, l: "2", a: 0},
{o: 99.48, f: 106.44, l: "8", a: 1},
{o: 106.44, f: 114.653, l: "7", a: 0},
{o: 114.653, f: 123.427, l: "2", a: 1},
{o: 123.427, f: 132.6, l: "1", a: 0},
{o: 132.6, f: 140.307, l: "4", a: 1},
{o: 140.307, f: 146.067, l: "2", a: 0},
{o: 146.067, f: 153.693, l: "3", a: 1},
{o: 153.693, f: 161.907, l: "1", a: 0},
{o: 161.907, f: 168.653, l: "2", a: 1},
{o: 168.653, f: 177.413, l: "3", a: 0},
{o: 177.413, f: 205.24, l: "1", a: 1},
{o: 205.24, f: 213.36, l: "2", a: 0},
{o: 213.36, f: 220.76, l: "1", a: 1},
{o: 220.76, f: 227.027, l: "2", a: 0},
{o: 227.027, f: 234.52, l: "3", a: 1},
{o: 234.52, f: 243.72, l: "2", a: 0},
{o: 243.72, f: 250.907, l: "4", a: 1},
{o: 250.907, f: 256.173, l: "1", a: 0},
{o: 256.173, f: 274.44, l: "2", a: 1},
{o: 274.44, f: 284.653, l: "6", a: 0}],
[{o: 0, f: 20.86, l: "a", a: 0},
{o: 20.86, f: 32.78, l: "b", a: 1},
{o: 32.78, f: 48.425, l: "b", a: 0},
{o: 48.425, f: 119.945, l: "c", a: 1},
{o: 119.945, f: 166.135, l: "d", a: 0},
{o: 166.135, f: 190.72, l: "e", a: 1},
{o: 190.72, f: 227.225, l: "d", a: 0},
{o: 227.225, f: 286.08, l: "f", a: 1}],
[{o: 0, f: 157.907, l: "n1", a: 0},
{o: 157.907, f: 169.691, l: "A", a: 1},
{o: 169.691, f: 219.336, l: "n2", a: 0},
{o: 219.336, f: 229.796, l: "A", a: 1},
{o: 229.796, f: 286.488, l: "n3", a: 0}],
[{o: 0, f: 0.256, l: "J", a: 0},
{o: 0.256, f: 5.332, l: "H", a: 1},
{o: 5.332, f: 65.548, l: "G", a: 0},
{o: 65.548, f: 91.856, l: "A", a: 1},
{o: 91.856, f: 154.7, l: "H", a: 0},
{o: 154.7, f: 173.108, l: "C", a: 1},
{o: 173.108, f: 221.052, l: "I", a: 0},
{o: 221.052, f: 240.332, l: "H", a: 1},
{o: 240.332, f: 274.22, l: "F", a: 0},
{o: 274.22, f: 278.812, l: "A", a: 1},
{o: 278.812, f: 286.512, l: "J", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001411.ogg";

var artist = "Compilations";

var track = "Farruca El Albacin ";
