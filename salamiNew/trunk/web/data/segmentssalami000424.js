var data = [
[{o: 0.07, f: 7.362, l: "A", a: 0},
{o: 7.362, f: 51.186, l: "B", a: 1},
{o: 51.186, f: 80.383, l: "C", a: 0},
{o: 80.383, f: 123.952, l: "B", a: 1},
{o: 123.952, f: 153.03, l: "D", a: 0},
{o: 153.03, f: 182.181, l: "C", a: 1},
{o: 182.181, f: 219.823, l: "C", a: 0}],
[{o: 0.1, f: 6.468, l: "E", a: 0},
{o: 6.468, f: 12.836, l: "C", a: 1},
{o: 12.836, f: 24.192, l: "C", a: 0},
{o: 24.192, f: 37.38, l: "C", a: 1},
{o: 37.38, f: 45.564, l: "C", a: 0},
{o: 45.564, f: 51.02, l: "D", a: 1},
{o: 51.02, f: 65.564, l: "A", a: 0},
{o: 65.564, f: 77.376, l: "B", a: 1},
{o: 77.376, f: 84.652, l: "C", a: 0},
{o: 84.652, f: 96.92, l: "C", a: 1},
{o: 96.92, f: 104.192, l: "C", a: 0},
{o: 104.192, f: 111.464, l: "F", a: 1},
{o: 111.464, f: 118.292, l: "G", a: 0},
{o: 118.292, f: 127.38, l: "D", a: 1},
{o: 127.38, f: 136.472, l: "H", a: 0},
{o: 136.472, f: 144.192, l: "I", a: 1},
{o: 144.192, f: 153.736, l: "J", a: 0},
{o: 153.736, f: 161.016, l: "A", a: 1},
{o: 161.016, f: 168.288, l: "A", a: 0},
{o: 168.288, f: 179.196, l: "B", a: 1},
{o: 179.196, f: 187.828, l: "A", a: 0},
{o: 187.828, f: 196.472, l: "A", a: 1},
{o: 196.472, f: 212.796, l: "B", a: 0}],
[{o: 0.1, f: 6.468, l: "D", a: 0},
{o: 6.468, f: 12.836, l: "E", a: 1},
{o: 12.836, f: 24.192, l: "C", a: 0},
{o: 24.192, f: 37.38, l: "C", a: 1},
{o: 37.38, f: 45.564, l: "F", a: 0},
{o: 45.564, f: 51.02, l: "G", a: 1},
{o: 51.02, f: 65.564, l: "A", a: 0},
{o: 65.564, f: 77.376, l: "B", a: 1},
{o: 77.376, f: 84.652, l: "C", a: 0},
{o: 84.652, f: 96.92, l: "C", a: 1},
{o: 96.92, f: 104.192, l: "C", a: 0},
{o: 104.192, f: 111.464, l: "H", a: 1},
{o: 111.464, f: 118.292, l: "I", a: 0},
{o: 118.292, f: 127.38, l: "J", a: 1},
{o: 127.38, f: 136.472, l: "K", a: 0},
{o: 136.472, f: 144.192, l: "L", a: 1},
{o: 144.192, f: 153.736, l: "M", a: 0},
{o: 153.736, f: 161.016, l: "A", a: 1},
{o: 161.016, f: 168.288, l: "A", a: 0},
{o: 168.288, f: 179.196, l: "B", a: 1},
{o: 179.196, f: 187.828, l: "N", a: 0},
{o: 187.828, f: 196.472, l: "A", a: 1},
{o: 196.472, f: 212.796, l: "O", a: 0}],
[{o: 0.573, f: 6.693, l: "8", a: 0},
{o: 6.693, f: 18.96, l: "1", a: 1},
{o: 18.96, f: 24.88, l: "6", a: 0},
{o: 24.88, f: 31.253, l: "4", a: 1},
{o: 31.253, f: 51.253, l: "1", a: 0},
{o: 51.253, f: 64.88, l: "2", a: 1},
{o: 64.88, f: 78.96, l: "3", a: 0},
{o: 78.96, f: 92.613, l: "1", a: 1},
{o: 92.613, f: 103.973, l: "4", a: 0},
{o: 103.973, f: 114.88, l: "1", a: 1},
{o: 114.88, f: 120.347, l: "6", a: 0},
{o: 120.347, f: 150.773, l: "5", a: 1},
{o: 150.773, f: 168.053, l: "2", a: 0},
{o: 168.053, f: 180.8, l: "3", a: 1},
{o: 180.8, f: 196.693, l: "2", a: 0},
{o: 196.693, f: 210.747, l: "3", a: 1},
{o: 210.747, f: 218.707, l: "7", a: 0}],
[{o: 0, f: 28.31, l: "a", a: 0},
{o: 28.31, f: 76.735, l: "b", a: 1},
{o: 76.735, f: 101.32, l: "a", a: 0},
{o: 101.32, f: 122.925, l: "a", a: 1},
{o: 122.925, f: 178.8, l: "b", a: 0},
{o: 178.8, f: 207.855, l: "a", a: 1},
{o: 207.855, f: 219.03, l: "c", a: 0}],
[{o: 0, f: 6.954, l: "n1", a: 0},
{o: 6.954, f: 28.77, l: "A", a: 1},
{o: 28.77, f: 51.061, l: "A", a: 0},
{o: 51.061, f: 79.679, l: "B", a: 1},
{o: 79.679, f: 101.494, l: "A", a: 0},
{o: 101.494, f: 121.498, l: "A", a: 1},
{o: 121.498, f: 152.857, l: "n6", a: 0},
{o: 152.857, f: 181.952, l: "B", a: 1},
{o: 181.952, f: 209.235, l: "B", a: 0},
{o: 209.235, f: 219.614, l: "n8", a: 1}],
[{o: 0, f: 0.004, l: "H", a: 0},
{o: 0.004, f: 9.5, l: "D", a: 1},
{o: 9.5, f: 24.956, l: "G", a: 0},
{o: 24.956, f: 36.62, l: "D", a: 1},
{o: 36.62, f: 46.92, l: "G", a: 0},
{o: 46.92, f: 81.468, l: "A", a: 1},
{o: 81.468, f: 97.528, l: "G", a: 0},
{o: 97.528, f: 109.496, l: "D", a: 1},
{o: 109.496, f: 119.5, l: "G", a: 0},
{o: 119.5, f: 125.408, l: "D", a: 1},
{o: 125.408, f: 153.132, l: "G", a: 0},
{o: 153.132, f: 209.044, l: "A", a: 1},
{o: 209.044, f: 211.892, l: "E", a: 0},
{o: 211.892, f: 219.773, l: "H", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000424.ogg";

var artist = "RWC MDB P 2001 M04";

var track = "5";
