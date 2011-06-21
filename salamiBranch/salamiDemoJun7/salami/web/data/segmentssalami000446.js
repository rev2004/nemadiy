var data = [
[{o: 0.025, f: 6.925, l: "A", a: 0},
{o: 6.925, f: 31.773, l: "B", a: 1},
{o: 31.773, f: 56.134, l: "B", a: 0},
{o: 56.134, f: 80.215, l: "B", a: 1},
{o: 80.215, f: 104.588, l: "B", a: 0},
{o: 104.588, f: 128.302, l: "B", a: 1},
{o: 128.302, f: 156.543, l: "B'", a: 0}],
[{o: 0.18, f: 10.708, l: "C", a: 0},
{o: 10.708, f: 26.804, l: "B", a: 1},
{o: 26.804, f: 42.28, l: "A", a: 0},
{o: 42.28, f: 54.232, l: "A", a: 1},
{o: 54.232, f: 69.216, l: "B", a: 0},
{o: 69.216, f: 76.804, l: "B", a: 1},
{o: 76.804, f: 95.396, l: "A", a: 0},
{o: 95.396, f: 119.336, l: "B", a: 1},
{o: 119.336, f: 133.552, l: "B", a: 0},
{o: 133.552, f: 147.476, l: "A", a: 1},
{o: 147.476, f: 156.456, l: "D", a: 0}],
[{o: 0.18, f: 10.708, l: "C", a: 0},
{o: 10.708, f: 26.804, l: "B", a: 1},
{o: 26.804, f: 42.28, l: "A", a: 0},
{o: 42.28, f: 54.232, l: "A", a: 1},
{o: 54.232, f: 69.216, l: "D", a: 0},
{o: 69.216, f: 76.804, l: "E", a: 1},
{o: 76.804, f: 95.396, l: "A", a: 0},
{o: 95.396, f: 119.336, l: "B", a: 1},
{o: 119.336, f: 133.552, l: "F", a: 0},
{o: 133.552, f: 147.476, l: "A", a: 1},
{o: 147.476, f: 156.456, l: "G", a: 0}],
[{o: 0.36, f: 1.32, l: "8", a: 0},
{o: 1.32, f: 13.2, l: "4", a: 1},
{o: 13.2, f: 21.2, l: "2", a: 0},
{o: 21.2, f: 25.867, l: "5", a: 1},
{o: 25.867, f: 36.04, l: "3", a: 0},
{o: 36.04, f: 40.973, l: "1", a: 1},
{o: 40.973, f: 45.187, l: "2", a: 0},
{o: 45.187, f: 54.387, l: "1", a: 1},
{o: 54.387, f: 60.36, l: "4", a: 0},
{o: 60.36, f: 69.413, l: "2", a: 1},
{o: 69.413, f: 73.96, l: "5", a: 0},
{o: 73.96, f: 82.693, l: "3", a: 1},
{o: 82.693, f: 101.667, l: "1", a: 0},
{o: 101.667, f: 109.493, l: "4", a: 1},
{o: 109.493, f: 114.36, l: "2", a: 0},
{o: 114.36, f: 121.373, l: "5", a: 1},
{o: 121.373, f: 133.32, l: "3", a: 0},
{o: 133.32, f: 148.16, l: "1", a: 1},
{o: 148.16, f: 153.8, l: "6", a: 0},
{o: 153.8, f: 156.44, l: "7", a: 1}],
[{o: 0, f: 51.405, l: "a", a: 0},
{o: 51.405, f: 101.32, l: "a", a: 1},
{o: 101.32, f: 148.255, l: "a", a: 0},
{o: 148.255, f: 155.705, l: "b", a: 1}],
[{o: 0, f: 4.644, l: "n1", a: 0},
{o: 4.644, f: 28.967, l: "A", a: 1},
{o: 28.967, f: 53.522, l: "A", a: 0},
{o: 53.522, f: 77.589, l: "A", a: 1},
{o: 77.589, f: 101.924, l: "A", a: 0},
{o: 101.924, f: 125.597, l: "A", a: 1},
{o: 125.597, f: 148.434, l: "A", a: 0},
{o: 148.434, f: 156.456, l: "n7", a: 1}],
[{o: 0, f: 0.012, l: "G", a: 0},
{o: 0.012, f: 13.344, l: "D", a: 1},
{o: 13.344, f: 34.176, l: "E", a: 0},
{o: 34.176, f: 41.184, l: "C", a: 1},
{o: 41.184, f: 52.7, l: "E", a: 0},
{o: 52.7, f: 59.832, l: "D", a: 1},
{o: 59.832, f: 77.956, l: "E", a: 0},
{o: 77.956, f: 89.516, l: "C", a: 1},
{o: 89.516, f: 100.216, l: "E", a: 0},
{o: 100.216, f: 111.768, l: "D", a: 1},
{o: 111.768, f: 125.904, l: "E", a: 0},
{o: 125.904, f: 137.652, l: "C", a: 1},
{o: 137.652, f: 146.708, l: "E", a: 0},
{o: 146.708, f: 156.456, l: "F", a: 1},
{o: 156.456, f: 156.527, l: "G", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000446.ogg";