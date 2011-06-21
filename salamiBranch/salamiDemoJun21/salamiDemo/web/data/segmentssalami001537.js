var data = [
[{o: 0.023, f: 14.745, l: "C", a: 0},
{o: 14.745, f: 41.074, l: "A", a: 1},
{o: 41.074, f: 54.105, l: "B", a: 0},
{o: 54.105, f: 80.081, l: "A", a: 1},
{o: 80.081, f: 93.187, l: "B", a: 0},
{o: 93.187, f: 118.073, l: "A", a: 1},
{o: 118.073, f: 130.673, l: "B", a: 0},
{o: 130.673, f: 156.969, l: "A", a: 1},
{o: 156.969, f: 170.33, l: "B", a: 0}],
[{o: 0.312, f: 13.884, l: "C", a: 0},
{o: 13.884, f: 28.56, l: "A", a: 1},
{o: 28.56, f: 33.552, l: "D", a: 0},
{o: 33.552, f: 47.488, l: "A", a: 1},
{o: 47.488, f: 53.996, l: "E", a: 0},
{o: 53.996, f: 66.904, l: "A", a: 1},
{o: 66.904, f: 73.436, l: "A", a: 0},
{o: 73.436, f: 81.692, l: "A", a: 1},
{o: 81.692, f: 89.904, l: "A", a: 0},
{o: 89.904, f: 103.316, l: "B", a: 1},
{o: 103.316, f: 112.564, l: "B", a: 0},
{o: 112.564, f: 128.956, l: "B", a: 1},
{o: 128.956, f: 146.508, l: "A", a: 0},
{o: 146.508, f: 161.456, l: "A", a: 1},
{o: 161.456, f: 170.22, l: "A", a: 0}],
[{o: 0.312, f: 13.884, l: "C", a: 0},
{o: 13.884, f: 28.56, l: "A", a: 1},
{o: 28.56, f: 33.552, l: "D", a: 0},
{o: 33.552, f: 47.488, l: "A", a: 1},
{o: 47.488, f: 53.996, l: "E", a: 0},
{o: 53.996, f: 66.904, l: "F", a: 1},
{o: 66.904, f: 73.436, l: "A", a: 0},
{o: 73.436, f: 81.692, l: "G", a: 1},
{o: 81.692, f: 89.904, l: "H", a: 0},
{o: 89.904, f: 103.316, l: "B", a: 1},
{o: 103.316, f: 112.564, l: "B", a: 0},
{o: 112.564, f: 128.956, l: "B", a: 1},
{o: 128.956, f: 146.508, l: "A", a: 0},
{o: 146.508, f: 161.456, l: "A", a: 1},
{o: 161.456, f: 170.22, l: "I", a: 0}],
[{o: 0.787, f: 11.853, l: "8", a: 0},
{o: 11.853, f: 24.867, l: "1", a: 1},
{o: 24.867, f: 43.84, l: "4", a: 0},
{o: 43.84, f: 58.36, l: "1", a: 1},
{o: 58.36, f: 76.347, l: "2", a: 0},
{o: 76.347, f: 91.853, l: "5", a: 1},
{o: 91.853, f: 103.48, l: "6", a: 0},
{o: 103.48, f: 126.947, l: "3", a: 1},
{o: 126.947, f: 142.48, l: "1", a: 0},
{o: 142.48, f: 158.88, l: "2", a: 1},
{o: 158.88, f: 170.227, l: "7", a: 0}],
[{o: 0, f: 10.43, l: "a", a: 0},
{o: 10.43, f: 36.505, l: "b", a: 1},
{o: 36.505, f: 49.17, l: "c", a: 0},
{o: 49.17, f: 74.5, l: "b", a: 1},
{o: 74.5, f: 88.655, l: "c", a: 0},
{o: 88.655, f: 126.65, l: "d", a: 1},
{o: 126.65, f: 154.215, l: "b", a: 0},
{o: 154.215, f: 169.86, l: "e", a: 1}],
[{o: 0, f: 10.704, l: "n1", a: 0},
{o: 10.704, f: 24.439, l: "B", a: 1},
{o: 24.439, f: 42.597, l: "A", a: 0},
{o: 42.597, f: 63.658, l: "n3", a: 1},
{o: 63.658, f: 81.212, l: "A", a: 0},
{o: 81.212, f: 126.63, l: "n4", a: 1},
{o: 126.63, f: 140.399, l: "B", a: 0},
{o: 140.399, f: 158.407, l: "A", a: 1},
{o: 158.407, f: 170.202, l: "n6", a: 0}],
[{o: 0, f: 0.008, l: "I", a: 0},
{o: 0.008, f: 17.268, l: "C", a: 1},
{o: 17.268, f: 38.12, l: "F", a: 0},
{o: 38.12, f: 56.712, l: "A", a: 1},
{o: 56.712, f: 77.164, l: "F", a: 0},
{o: 77.164, f: 102.508, l: "A", a: 1},
{o: 102.508, f: 116.236, l: "D", a: 0},
{o: 116.236, f: 133.612, l: "H", a: 1},
{o: 133.612, f: 152.672, l: "F", a: 0},
{o: 152.672, f: 169.916, l: "G", a: 1},
{o: 169.916, f: 170.252, l: "F", a: 0},
{o: 170.252, f: 170.319, l: "I", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001537.ogg";

var artist = "Compilations";

var track = "Me and My Crazy Self";
