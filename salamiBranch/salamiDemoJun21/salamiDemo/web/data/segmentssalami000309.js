var data = [
[{o: 0.052, f: 22.872, l: "A", a: 0},
{o: 22.872, f: 58.539, l: "B", a: 1},
{o: 58.539, f: 70.411, l: "C", a: 0},
{o: 70.411, f: 106.545, l: "B", a: 1},
{o: 106.545, f: 118.518, l: "C", a: 0},
{o: 118.518, f: 289.582, l: "D", a: 1},
{o: 289.582, f: 336.213, l: "E", a: 0},
{o: 336.213, f: 371.76, l: "B", a: 1},
{o: 371.76, f: 389.967, l: "C", a: 0},
{o: 389.967, f: 427.083, l: "Z", a: 1}],
[{o: 0, f: 427, l: "A", a: 0}],
[{o: 0.02, f: 8.356, l: "B", a: 0},
{o: 8.356, f: 25.412, l: "C", a: 1},
{o: 25.412, f: 42.128, l: "C", a: 0},
{o: 42.128, f: 56.292, l: "C", a: 1},
{o: 56.292, f: 70.356, l: "C", a: 0},
{o: 70.356, f: 82.764, l: "C", a: 1},
{o: 82.764, f: 97.092, l: "C", a: 0},
{o: 97.092, f: 109.852, l: "C", a: 1},
{o: 109.852, f: 123.256, l: "C", a: 0},
{o: 123.256, f: 139.644, l: "C", a: 1},
{o: 139.644, f: 155.248, l: "C", a: 0},
{o: 155.248, f: 181.58, l: "C", a: 1},
{o: 181.58, f: 198.464, l: "D", a: 0},
{o: 198.464, f: 207.076, l: "D", a: 1},
{o: 207.076, f: 227.176, l: "E", a: 0},
{o: 227.176, f: 244.796, l: "F", a: 1},
{o: 244.796, f: 258.648, l: "C", a: 0},
{o: 258.648, f: 283.632, l: "C", a: 1},
{o: 283.632, f: 297.572, l: "C", a: 0},
{o: 297.572, f: 321.448, l: "G", a: 1},
{o: 321.448, f: 334.644, l: "H", a: 0},
{o: 334.644, f: 349.448, l: "A", a: 1},
{o: 349.448, f: 363.888, l: "A", a: 0},
{o: 363.888, f: 386.372, l: "I", a: 1},
{o: 386.372, f: 397.932, l: "J", a: 0},
{o: 397.932, f: 414.044, l: "K", a: 1},
{o: 414.044, f: 426.928, l: "L", a: 0}],
[{o: 0.44, f: 4.907, l: "3", a: 0},
{o: 4.907, f: 17.587, l: "1", a: 1},
{o: 17.587, f: 23.4, l: "2", a: 0},
{o: 23.4, f: 28.52, l: "1", a: 1},
{o: 28.52, f: 69.827, l: "2", a: 0},
{o: 69.827, f: 75.053, l: "1", a: 1},
{o: 75.053, f: 117.853, l: "2", a: 0},
{o: 117.853, f: 143.253, l: "4", a: 1},
{o: 143.253, f: 157.707, l: "3", a: 0},
{o: 157.707, f: 205.547, l: "1", a: 1},
{o: 205.547, f: 209.333, l: "4", a: 0},
{o: 209.333, f: 230.76, l: "5", a: 1},
{o: 230.76, f: 245.4, l: "3", a: 0},
{o: 245.4, f: 310.373, l: "1", a: 1},
{o: 310.373, f: 332.253, l: "2", a: 0},
{o: 332.253, f: 335.96, l: "8", a: 1},
{o: 335.96, f: 383.24, l: "2", a: 0},
{o: 383.24, f: 389.88, l: "1", a: 1},
{o: 389.88, f: 404.627, l: "6", a: 0},
{o: 404.627, f: 415.72, l: "7", a: 1},
{o: 415.72, f: 422.827, l: "5", a: 0},
{o: 422.827, f: 426.893, l: "6", a: 1}],
[{o: 0, f: 19.37, l: "a", a: 0},
{o: 19.37, f: 67.795, l: "b", a: 1},
{o: 67.795, f: 116.965, l: "b", a: 0},
{o: 116.965, f: 147.51, l: "c", a: 1},
{o: 147.51, f: 167.625, l: "d", a: 0},
{o: 167.625, f: 194.445, l: "e", a: 1},
{o: 194.445, f: 220.52, l: "e", a: 0},
{o: 220.52, f: 241.38, l: "d", a: 1},
{o: 241.38, f: 271.925, l: "e", a: 0},
{o: 271.925, f: 336.74, l: "a", a: 1},
{o: 336.74, f: 382.93, l: "b", a: 0},
{o: 382.93, f: 426.885, l: "f", a: 1}],
[{o: 0, f: 11.018, l: "n1", a: 0},
{o: 11.018, f: 23.626, l: "A", a: 1},
{o: 23.626, f: 35.468, l: "A", a: 0},
{o: 35.468, f: 46.243, l: "A", a: 1},
{o: 46.243, f: 59.327, l: "B", a: 0},
{o: 59.327, f: 71.169, l: "C", a: 1},
{o: 71.169, f: 83.174, l: "A", a: 0},
{o: 83.174, f: 94.11, l: "A", a: 1},
{o: 94.11, f: 105.744, l: "B", a: 0},
{o: 105.744, f: 116.216, l: "A", a: 1},
{o: 116.216, f: 157.141, l: "n9", a: 0},
{o: 157.141, f: 169.808, l: "D", a: 1},
{o: 169.808, f: 183.182, l: "n10", a: 0},
{o: 183.182, f: 194.351, l: "A", a: 1},
{o: 194.351, f: 213.391, l: "n11", a: 0},
{o: 213.391, f: 224.096, l: "A", a: 1},
{o: 224.096, f: 231.166, l: "n12", a: 0},
{o: 231.166, f: 242.649, l: "A", a: 1},
{o: 242.649, f: 248.767, l: "n13", a: 0},
{o: 248.767, f: 264.115, l: "D", a: 1},
{o: 264.115, f: 276.434, l: "A", a: 0},
{o: 276.434, f: 286.337, l: "n15", a: 1},
{o: 286.337, f: 296.902, l: "A", a: 0},
{o: 296.902, f: 312.436, l: "D", a: 1},
{o: 312.436, f: 325.149, l: "B", a: 0},
{o: 325.149, f: 336.91, l: "C", a: 1},
{o: 336.91, f: 348.729, l: "A", a: 0},
{o: 348.729, f: 359.317, l: "A", a: 1},
{o: 359.317, f: 372.657, l: "B", a: 0},
{o: 372.657, f: 384.186, l: "C", a: 1},
{o: 384.186, f: 426.922, l: "n21", a: 0}],
[{o: 0, f: 0.012, l: "G", a: 0},
{o: 0.012, f: 33, l: "B", a: 1},
{o: 33, f: 64.256, l: "F", a: 0},
{o: 64.256, f: 82.58, l: "B", a: 1},
{o: 82.58, f: 123.288, l: "F", a: 0},
{o: 123.288, f: 145.832, l: "C", a: 1},
{o: 145.832, f: 161.248, l: "F", a: 0},
{o: 161.248, f: 201.256, l: "B", a: 1},
{o: 201.256, f: 236.5, l: "F", a: 0},
{o: 236.5, f: 264.76, l: "B", a: 1},
{o: 264.76, f: 277.64, l: "F", a: 0},
{o: 277.64, f: 345.556, l: "B", a: 1},
{o: 345.556, f: 389.128, l: "F", a: 0},
{o: 389.128, f: 405.168, l: "C", a: 1},
{o: 405.168, f: 423.356, l: "E", a: 0},
{o: 423.356, f: 426.932, l: "C", a: 1},
{o: 426.932, f: 427, l: "G", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000309.ogg";

var artist = "John Mullins";

var track = "Intrepid Traveler";
