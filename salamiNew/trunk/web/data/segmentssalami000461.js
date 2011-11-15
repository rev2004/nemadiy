var data = [
[{o: 0.58, f: 22.384, l: "A", a: 0},
{o: 22.384, f: 40.18, l: "A", a: 1},
{o: 40.18, f: 101.471, l: "B", a: 0},
{o: 101.471, f: 113.348, l: "A'", a: 1},
{o: 113.348, f: 165.616, l: "C", a: 0},
{o: 165.616, f: 178.286, l: "A", a: 1},
{o: 178.286, f: 190.565, l: "A''", a: 0}],
[{o: 0.412, f: 5.956, l: "D", a: 0},
{o: 5.956, f: 23.848, l: "B", a: 1},
{o: 23.848, f: 38.564, l: "B", a: 0},
{o: 38.564, f: 52.884, l: "B", a: 1},
{o: 52.884, f: 70.044, l: "A", a: 0},
{o: 70.044, f: 80.98, l: "A", a: 1},
{o: 80.98, f: 101.484, l: "A", a: 0},
{o: 101.484, f: 110.748, l: "A", a: 1},
{o: 110.748, f: 123.896, l: "A", a: 0},
{o: 123.896, f: 137.668, l: "A", a: 1},
{o: 137.668, f: 148.832, l: "E", a: 0},
{o: 148.832, f: 158.244, l: "C", a: 1},
{o: 158.244, f: 166.7, l: "C", a: 0},
{o: 166.7, f: 182.492, l: "F", a: 1}],
[{o: 0.412, f: 5.956, l: "D", a: 0},
{o: 5.956, f: 23.848, l: "B", a: 1},
{o: 23.848, f: 38.564, l: "B", a: 0},
{o: 38.564, f: 52.884, l: "B", a: 1},
{o: 52.884, f: 70.044, l: "A", a: 0},
{o: 70.044, f: 80.98, l: "A", a: 1},
{o: 80.98, f: 101.484, l: "A", a: 0},
{o: 101.484, f: 110.748, l: "E", a: 1},
{o: 110.748, f: 123.896, l: "C", a: 0},
{o: 123.896, f: 137.668, l: "C", a: 1},
{o: 137.668, f: 148.832, l: "F", a: 0},
{o: 148.832, f: 158.244, l: "G", a: 1},
{o: 158.244, f: 166.7, l: "H", a: 0},
{o: 166.7, f: 182.492, l: "I", a: 1}],
[{o: 0.28, f: 2.733, l: "8", a: 0},
{o: 2.733, f: 16.853, l: "3", a: 1},
{o: 16.853, f: 22.707, l: "1", a: 0},
{o: 22.707, f: 30.813, l: "3", a: 1},
{o: 30.813, f: 48.093, l: "1", a: 0},
{o: 48.093, f: 53.48, l: "3", a: 1},
{o: 53.48, f: 59.453, l: "1", a: 0},
{o: 59.453, f: 66.813, l: "3", a: 1},
{o: 66.813, f: 80.4, l: "1", a: 0},
{o: 80.4, f: 87.453, l: "6", a: 1},
{o: 87.453, f: 94.547, l: "4", a: 0},
{o: 94.547, f: 100.173, l: "1", a: 1},
{o: 100.173, f: 155.16, l: "2", a: 0},
{o: 155.16, f: 159.933, l: "7", a: 1},
{o: 159.933, f: 164.6, l: "4", a: 0},
{o: 164.6, f: 178.08, l: "2", a: 1},
{o: 178.08, f: 188.893, l: "5", a: 0}],
[{o: 0, f: 0.745, l: "a", a: 0},
{o: 0.745, f: 22.35, l: "b", a: 1},
{o: 22.35, f: 59.6, l: "c", a: 0},
{o: 59.6, f: 90.89, l: "d", a: 1},
{o: 90.89, f: 107.28, l: "e", a: 0},
{o: 107.28, f: 123.67, l: "e", a: 1},
{o: 123.67, f: 152.725, l: "f", a: 0},
{o: 152.725, f: 189.975, l: "c", a: 1}],
[{o: 0, f: 190.137, l: "A", a: 0},
{o: 0.267, f: 190.264, l: "A", a: 1}],
[{o: 0, f: 0.504, l: "H", a: 0},
{o: 0.504, f: 11.648, l: "F", a: 1},
{o: 11.648, f: 24.032, l: "G", a: 0},
{o: 24.032, f: 33.516, l: "B", a: 1},
{o: 33.516, f: 45.944, l: "F", a: 0},
{o: 45.944, f: 65.752, l: "A", a: 1},
{o: 65.752, f: 74.584, l: "B", a: 0},
{o: 74.584, f: 101.3, l: "A", a: 1},
{o: 101.3, f: 146.108, l: "F", a: 0},
{o: 146.108, f: 166.644, l: "A", a: 1},
{o: 166.644, f: 172.656, l: "F", a: 0},
{o: 172.656, f: 182.424, l: "E", a: 1},
{o: 182.424, f: 190.564, l: "H", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000461.ogg";

var artist = "Ornette Coleman";

var track = "Mob Job";
