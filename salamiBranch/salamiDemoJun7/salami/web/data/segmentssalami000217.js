var data = [
[{o: 0.234, f: 62.946, l: "A", a: 0},
{o: 62.946, f: 98.921, l: "B", a: 1},
{o: 98.921, f: 134.615, l: "C", a: 0},
{o: 134.615, f: 179.548, l: "D", a: 1},
{o: 179.548, f: 209.436, l: "A", a: 0},
{o: 209.436, f: 242.338, l: "E", a: 1}],
[{o: 0.032, f: 15.74, l: "A", a: 0},
{o: 15.74, f: 35.656, l: "A", a: 1},
{o: 35.656, f: 51.716, l: "A", a: 0},
{o: 51.716, f: 65.588, l: "A", a: 1},
{o: 65.588, f: 79.496, l: "A", a: 0},
{o: 79.496, f: 92.172, l: "D", a: 1},
{o: 92.172, f: 102.392, l: "D", a: 0},
{o: 102.392, f: 123.656, l: "A", a: 1},
{o: 123.656, f: 132.328, l: "D", a: 0},
{o: 132.328, f: 139.796, l: "D", a: 1},
{o: 139.796, f: 162.588, l: "D", a: 0},
{o: 162.588, f: 174.124, l: "D", a: 1},
{o: 174.124, f: 189.96, l: "B", a: 0},
{o: 189.96, f: 199.084, l: "A", a: 1},
{o: 199.084, f: 209.612, l: "A", a: 0},
{o: 209.612, f: 221.952, l: "A", a: 1},
{o: 221.952, f: 237.588, l: "A", a: 0}],
[{o: 0.032, f: 15.74, l: "A", a: 0},
{o: 15.74, f: 35.656, l: "A", a: 1},
{o: 35.656, f: 51.716, l: "G", a: 0},
{o: 51.716, f: 65.588, l: "G", a: 1},
{o: 65.588, f: 79.496, l: "F", a: 0},
{o: 79.496, f: 92.172, l: "D", a: 1},
{o: 92.172, f: 102.392, l: "D", a: 0},
{o: 102.392, f: 123.656, l: "F", a: 1},
{o: 123.656, f: 132.328, l: "E", a: 0},
{o: 132.328, f: 139.796, l: "B", a: 1},
{o: 139.796, f: 162.588, l: "B", a: 0},
{o: 162.588, f: 174.124, l: "H", a: 1},
{o: 174.124, f: 189.96, l: "I", a: 0},
{o: 189.96, f: 199.084, l: "A", a: 1},
{o: 199.084, f: 209.612, l: "A", a: 0},
{o: 209.612, f: 221.952, l: "C", a: 1},
{o: 221.952, f: 237.588, l: "C", a: 0}],
[{o: 0.733, f: 14.453, l: "1", a: 0},
{o: 14.453, f: 24.8, l: "4", a: 1},
{o: 24.8, f: 42.387, l: "1", a: 0},
{o: 42.387, f: 60.36, l: "5", a: 1},
{o: 60.36, f: 78.787, l: "1", a: 0},
{o: 78.787, f: 89.987, l: "3", a: 1},
{o: 89.987, f: 97.453, l: "4", a: 0},
{o: 97.453, f: 122.28, l: "2", a: 1},
{o: 122.28, f: 132.453, l: "4", a: 0},
{o: 132.453, f: 144.547, l: "3", a: 1},
{o: 144.547, f: 157.853, l: "2", a: 0},
{o: 157.853, f: 169.773, l: "3", a: 1},
{o: 169.773, f: 185.853, l: "1", a: 0},
{o: 185.853, f: 193.733, l: "5", a: 1},
{o: 193.733, f: 204.173, l: "2", a: 0},
{o: 204.173, f: 211.213, l: "8", a: 1},
{o: 211.213, f: 234.827, l: "6", a: 0},
{o: 234.827, f: 242.227, l: "7", a: 1}],
[{o: 0, f: 23.095, l: "a", a: 0},
{o: 23.095, f: 58.11, l: "b", a: 1},
{o: 58.11, f: 166.135, l: "c", a: 0},
{o: 166.135, f: 204.13, l: "b", a: 1},
{o: 204.13, f: 226.48, l: "d", a: 0},
{o: 226.48, f: 242.125, l: "e", a: 1}],
[{o: 0, f: 30.894, l: "n1", a: 0},
{o: 30.894, f: 47.659, l: "A", a: 1},
{o: 47.659, f: 174.997, l: "n2", a: 0},
{o: 174.997, f: 192.923, l: "A", a: 1},
{o: 192.923, f: 242.277, l: "n3", a: 0}],
[{o: 0, f: 0.032, l: "H", a: 0},
{o: 0.032, f: 57.828, l: "G", a: 1},
{o: 57.828, f: 71.652, l: "E", a: 0},
{o: 71.652, f: 95.796, l: "G", a: 1},
{o: 95.796, f: 114.092, l: "D", a: 0},
{o: 114.092, f: 168.324, l: "A", a: 1},
{o: 168.324, f: 237.588, l: "G", a: 0},
{o: 237.588, f: 242.339, l: "H", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000217.ogg";
