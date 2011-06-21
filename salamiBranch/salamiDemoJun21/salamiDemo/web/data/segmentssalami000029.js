var data = [
[{o: 3.486, f: 30.055, l: "Z", a: 0},
{o: 30.055, f: 53.271, l: "A", a: 1},
{o: 53.271, f: 88.332, l: "V", a: 0},
{o: 88.332, f: 103.872, l: "C", a: 1},
{o: 103.872, f: 111.41, l: "A", a: 0},
{o: 111.41, f: 138.889, l: "V", a: 1},
{o: 138.889, f: 154.468, l: "C", a: 0},
{o: 154.468, f: 171.315, l: "D", a: 1},
{o: 171.315, f: 188.948, l: "A", a: 0},
{o: 188.948, f: 216.244, l: "V", a: 1},
{o: 216.244, f: 231.68, l: "C", a: 0},
{o: 231.68, f: 248.927, l: "C", a: 1},
{o: 248.927, f: 260.745, l: "Z", a: 0}],
[{o: 0.764, f: 14.116, l: "D", a: 0},
{o: 14.116, f: 31.316, l: "A", a: 1},
{o: 31.316, f: 52.928, l: "A", a: 0},
{o: 52.928, f: 68.288, l: "A", a: 1},
{o: 68.288, f: 79.052, l: "A", a: 0},
{o: 79.052, f: 86.66, l: "E", a: 1},
{o: 86.66, f: 102.72, l: "A", a: 0},
{o: 102.72, f: 120.644, l: "A", a: 1},
{o: 120.644, f: 139.996, l: "E", a: 0},
{o: 139.996, f: 153.28, l: "A", a: 1},
{o: 153.28, f: 164, l: "E", a: 0},
{o: 164, f: 174.404, l: "A", a: 1},
{o: 174.404, f: 182.14, l: "E", a: 0},
{o: 182.14, f: 188.984, l: "E", a: 1},
{o: 188.984, f: 198.84, l: "A", a: 0},
{o: 198.84, f: 207.664, l: "E", a: 1},
{o: 207.664, f: 221.128, l: "A", a: 0},
{o: 221.128, f: 231.168, l: "B", a: 1},
{o: 231.168, f: 245.952, l: "D", a: 0},
{o: 245.952, f: 256.368, l: "D", a: 1}],
[{o: 0.764, f: 14.116, l: "D", a: 0},
{o: 14.116, f: 31.316, l: "A", a: 1},
{o: 31.316, f: 52.928, l: "C", a: 0},
{o: 52.928, f: 68.288, l: "B", a: 1},
{o: 68.288, f: 79.052, l: "B", a: 0},
{o: 79.052, f: 86.66, l: "E", a: 1},
{o: 86.66, f: 102.72, l: "B", a: 0},
{o: 102.72, f: 120.644, l: "B", a: 1},
{o: 120.644, f: 139.996, l: "E", a: 0},
{o: 139.996, f: 153.28, l: "B", a: 1},
{o: 153.28, f: 164, l: "E", a: 0},
{o: 164, f: 174.404, l: "A", a: 1},
{o: 174.404, f: 182.14, l: "F", a: 0},
{o: 182.14, f: 188.984, l: "G", a: 1},
{o: 188.984, f: 198.84, l: "A", a: 0},
{o: 198.84, f: 207.664, l: "E", a: 1},
{o: 207.664, f: 221.128, l: "H", a: 0},
{o: 221.128, f: 231.168, l: "I", a: 1},
{o: 231.168, f: 245.952, l: "J", a: 0},
{o: 245.952, f: 256.368, l: "D", a: 1}],
[{o: 0.68, f: 4.627, l: "8", a: 0},
{o: 4.627, f: 11.48, l: "5", a: 1},
{o: 11.48, f: 31.32, l: "4", a: 0},
{o: 31.32, f: 52.427, l: "3", a: 1},
{o: 52.427, f: 60.387, l: "1", a: 0},
{o: 60.387, f: 68.507, l: "3", a: 1},
{o: 68.507, f: 86.173, l: "1", a: 0},
{o: 86.173, f: 100.28, l: "2", a: 1},
{o: 100.28, f: 112.067, l: "3", a: 0},
{o: 112.067, f: 137.32, l: "1", a: 1},
{o: 137.32, f: 153.64, l: "2", a: 0},
{o: 153.64, f: 161.347, l: "4", a: 1},
{o: 161.347, f: 167.773, l: "2", a: 0},
{o: 167.773, f: 174.2, l: "1", a: 1},
{o: 174.2, f: 188.28, l: "6", a: 0},
{o: 188.28, f: 211.853, l: "1", a: 1},
{o: 211.853, f: 247.507, l: "2", a: 0},
{o: 247.507, f: 252.893, l: "5", a: 1},
{o: 252.893, f: 260.493, l: "7", a: 0}],
[{o: 0, f: 46.19, l: "a", a: 0},
{o: 46.19, f: 60.345, l: "b", a: 1},
{o: 60.345, f: 103.555, l: "c", a: 0},
{o: 103.555, f: 154.215, l: "c", a: 1},
{o: 154.215, f: 196.68, l: "d", a: 0},
{o: 196.68, f: 239.89, l: "c", a: 1},
{o: 239.89, f: 260.005, l: "e", a: 0}],
[{o: 0, f: 47.705, l: "n1", a: 0},
{o: 47.705, f: 63.065, l: "B", a: 1},
{o: 63.065, f: 78.576, l: "B", a: 0},
{o: 78.576, f: 105.883, l: "A", a: 1},
{o: 105.883, f: 121.371, l: "B", a: 0},
{o: 121.371, f: 129.254, l: "n2", a: 1},
{o: 129.254, f: 156.793, l: "A", a: 0},
{o: 156.793, f: 189.243, l: "n3", a: 1},
{o: 189.243, f: 196.661, l: "C", a: 0},
{o: 196.661, f: 203.685, l: "C", a: 1},
{o: 203.685, f: 208.062, l: "n5", a: 0},
{o: 208.062, f: 233.929, l: "A", a: 1},
{o: 233.929, f: 260.481, l: "n6", a: 0}],
[{o: 0, f: 0.764, l: "I", a: 0},
{o: 0.764, f: 19.404, l: "B", a: 1},
{o: 19.404, f: 30.104, l: "H", a: 0},
{o: 30.104, f: 86.892, l: "A", a: 1},
{o: 86.892, f: 103.66, l: "C", a: 0},
{o: 103.66, f: 138.516, l: "A", a: 1},
{o: 138.516, f: 156.928, l: "C", a: 0},
{o: 156.928, f: 215.952, l: "A", a: 1},
{o: 215.952, f: 247.184, l: "C", a: 0},
{o: 247.184, f: 256.368, l: "B", a: 1},
{o: 256.368, f: 260.638, l: "I", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000029.ogg";

var artist = "Tori Amos";

var track = "Honey Live ";
