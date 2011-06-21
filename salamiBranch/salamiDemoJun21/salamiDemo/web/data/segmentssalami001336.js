var data = [
[{o: 0.155, f: 10.466, l: "N", a: 0},
{o: 10.466, f: 24.302, l: "X", a: 1},
{o: 24.302, f: 31.225, l: "B", a: 0},
{o: 31.225, f: 38.147, l: "X", a: 1},
{o: 38.147, f: 45.045, l: "A", a: 0},
{o: 45.045, f: 51.951, l: "X", a: 1},
{o: 51.951, f: 58.902, l: "A", a: 0},
{o: 58.902, f: 65.815, l: "X", a: 1},
{o: 65.815, f: 72.722, l: "B", a: 0},
{o: 72.722, f: 79.637, l: "X", a: 1},
{o: 79.637, f: 86.543, l: "X", a: 0},
{o: 86.543, f: 93.465, l: "A", a: 1},
{o: 93.465, f: 100.363, l: "X", a: 0},
{o: 100.363, f: 107.298, l: "A", a: 1},
{o: 107.298, f: 114.2, l: "X", a: 0},
{o: 114.2, f: 121.114, l: "B", a: 1},
{o: 121.114, f: 128.024, l: "X", a: 0},
{o: 128.024, f: 134.988, l: "B", a: 1},
{o: 134.988, f: 141.845, l: "X", a: 0},
{o: 141.845, f: 148.776, l: "C", a: 1},
{o: 148.776, f: 155.682, l: "X", a: 0},
{o: 155.682, f: 162.596, l: "X", a: 1},
{o: 162.596, f: 169.522, l: "X", a: 0},
{o: 169.522, f: 176.453, l: "A", a: 1},
{o: 176.453, f: 183.335, l: "X", a: 0},
{o: 183.335, f: 190.272, l: "A", a: 1},
{o: 190.272, f: 197.184, l: "X", a: 0},
{o: 197.184, f: 204.086, l: "B", a: 1},
{o: 204.086, f: 211.036, l: "X", a: 0},
{o: 211.036, f: 217.935, l: "B", a: 1},
{o: 217.935, f: 224.849, l: "X", a: 0},
{o: 224.849, f: 231.776, l: "B", a: 1},
{o: 231.776, f: 238.703, l: "X", a: 0},
{o: 238.703, f: 245.615, l: "B", a: 1},
{o: 245.615, f: 258.516, l: "X", a: 0}],
[{o: 0.076, f: 9.536, l: "B", a: 0},
{o: 9.536, f: 23.788, l: "C", a: 1},
{o: 23.788, f: 37.62, l: "C", a: 0},
{o: 37.62, f: 47.144, l: "C", a: 1},
{o: 47.144, f: 57.952, l: "C", a: 0},
{o: 57.952, f: 65.284, l: "C", a: 1},
{o: 65.284, f: 72.196, l: "C", a: 0},
{o: 72.196, f: 85.612, l: "C", a: 1},
{o: 85.612, f: 95.552, l: "C", a: 0},
{o: 95.552, f: 114.12, l: "C", a: 1},
{o: 114.12, f: 123.608, l: "C", a: 0},
{o: 123.608, f: 132.716, l: "C", a: 1},
{o: 132.716, f: 141.768, l: "C", a: 0},
{o: 141.768, f: 148.704, l: "A", a: 1},
{o: 148.704, f: 156.044, l: "A", a: 0},
{o: 156.044, f: 172.464, l: "D", a: 1},
{o: 172.464, f: 184.14, l: "E", a: 0},
{o: 184.14, f: 190.628, l: "F", a: 1},
{o: 190.628, f: 199.708, l: "G", a: 0},
{o: 199.708, f: 205.328, l: "H", a: 1},
{o: 205.328, f: 217.432, l: "C", a: 0},
{o: 217.432, f: 230.412, l: "C", a: 1},
{o: 230.412, f: 242.944, l: "C", a: 0},
{o: 242.944, f: 252.024, l: "C", a: 1},
{o: 252.024, f: 257.672, l: "I", a: 0}],
[{o: 0.076, f: 9.536, l: "A", a: 0},
{o: 9.536, f: 23.788, l: "D", a: 1},
{o: 23.788, f: 37.62, l: "E", a: 0},
{o: 37.62, f: 47.144, l: "F", a: 1},
{o: 47.144, f: 57.952, l: "B", a: 0},
{o: 57.952, f: 65.284, l: "B", a: 1},
{o: 65.284, f: 72.196, l: "C", a: 0},
{o: 72.196, f: 85.612, l: "C", a: 1},
{o: 85.612, f: 95.552, l: "B", a: 0},
{o: 95.552, f: 114.12, l: "B", a: 1},
{o: 114.12, f: 123.608, l: "C", a: 0},
{o: 123.608, f: 132.716, l: "C", a: 1},
{o: 132.716, f: 141.768, l: "C", a: 0},
{o: 141.768, f: 148.704, l: "G", a: 1},
{o: 148.704, f: 156.044, l: "H", a: 0},
{o: 156.044, f: 172.464, l: "I", a: 1},
{o: 172.464, f: 184.14, l: "J", a: 0},
{o: 184.14, f: 190.628, l: "K", a: 1},
{o: 190.628, f: 199.708, l: "L", a: 0},
{o: 199.708, f: 205.328, l: "M", a: 1},
{o: 205.328, f: 217.432, l: "C", a: 0},
{o: 217.432, f: 230.412, l: "C", a: 1},
{o: 230.412, f: 242.944, l: "C", a: 0},
{o: 242.944, f: 252.024, l: "N", a: 1},
{o: 252.024, f: 257.672, l: "O", a: 0}],
[{o: 0.467, f: 2.84, l: "8", a: 0},
{o: 2.84, f: 11.48, l: "4", a: 1},
{o: 11.48, f: 17.107, l: "7", a: 0},
{o: 17.107, f: 24.013, l: "5", a: 1},
{o: 24.013, f: 63.8, l: "1", a: 0},
{o: 63.8, f: 69.4, l: "2", a: 1},
{o: 69.4, f: 111.32, l: "1", a: 0},
{o: 111.32, f: 117.8, l: "2", a: 1},
{o: 117.8, f: 126.453, l: "1", a: 0},
{o: 126.453, f: 131.64, l: "2", a: 1},
{o: 131.64, f: 169.667, l: "1", a: 0},
{o: 169.667, f: 199.493, l: "3", a: 1},
{o: 199.493, f: 223.293, l: "1", a: 0},
{o: 223.293, f: 228.467, l: "2", a: 1},
{o: 228.467, f: 237.12, l: "1", a: 0},
{o: 237.12, f: 242.307, l: "2", a: 1},
{o: 242.307, f: 251.4, l: "1", a: 0},
{o: 251.4, f: 257.72, l: "6", a: 1}],
[{o: 0, f: 32.78, l: "a", a: 0},
{o: 32.78, f: 64.815, l: "b", a: 1},
{o: 64.815, f: 81.205, l: "c", a: 0},
{o: 81.205, f: 113.24, l: "b", a: 1},
{o: 113.24, f: 129.63, l: "c", a: 0},
{o: 129.63, f: 145.275, l: "d", a: 1},
{o: 145.275, f: 169.115, l: "e", a: 0},
{o: 169.115, f: 198.17, l: "b", a: 1},
{o: 198.17, f: 209.345, l: "f", a: 0},
{o: 209.345, f: 225.735, l: "d", a: 1},
{o: 225.735, f: 236.91, l: "f", a: 0},
{o: 236.91, f: 257.77, l: "d", a: 1}],
[{o: 0, f: 24.694, l: "n1", a: 0},
{o: 24.694, f: 38.522, l: "A", a: 1},
{o: 38.522, f: 52.361, l: "B", a: 0},
{o: 52.361, f: 66.188, l: "B", a: 1},
{o: 66.188, f: 80.016, l: "A", a: 0},
{o: 80.016, f: 86.947, l: "n2", a: 1},
{o: 86.947, f: 100.763, l: "B", a: 0},
{o: 100.763, f: 114.59, l: "B", a: 1},
{o: 114.59, f: 128.43, l: "A", a: 0},
{o: 128.43, f: 142.245, l: "A", a: 1},
{o: 142.245, f: 169.912, l: "n3", a: 0},
{o: 169.912, f: 183.74, l: "B", a: 1},
{o: 183.74, f: 197.567, l: "B", a: 0},
{o: 197.567, f: 211.418, l: "A", a: 1},
{o: 211.418, f: 225.257, l: "A", a: 0},
{o: 225.257, f: 239.084, l: "A", a: 1},
{o: 239.084, f: 252.923, l: "A", a: 0},
{o: 252.923, f: 258.252, l: "n4", a: 1}],
[{o: 0, f: 0.072, l: "F", a: 0},
{o: 0.072, f: 13.856, l: "B", a: 1},
{o: 13.856, f: 22.504, l: "E", a: 0},
{o: 22.504, f: 36.556, l: "D", a: 1},
{o: 36.556, f: 125.144, l: "E", a: 0},
{o: 125.144, f: 170.532, l: "D", a: 1},
{o: 170.532, f: 197.328, l: "E", a: 0},
{o: 197.328, f: 209.444, l: "D", a: 1},
{o: 209.444, f: 220.68, l: "E", a: 0},
{o: 220.68, f: 252.472, l: "D", a: 1},
{o: 252.472, f: 256.804, l: "E", a: 0},
{o: 256.804, f: 258.427, l: "F", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001336.ogg";

var artist = "Compilations";

var track = "BEAT IT";