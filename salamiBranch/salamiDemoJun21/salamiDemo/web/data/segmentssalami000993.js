var data = [
[{o: 0.221, f: 7.038, l: "N", a: 0},
{o: 7.038, f: 32.625, l: "A", a: 1},
{o: 32.625, f: 58.106, l: "B", a: 0},
{o: 58.106, f: 83.911, l: "A'", a: 1},
{o: 83.911, f: 90.383, l: "C", a: 0},
{o: 90.383, f: 116.262, l: "D", a: 1},
{o: 116.262, f: 141.888, l: "D'", a: 0},
{o: 141.888, f: 167.877, l: "D", a: 1},
{o: 167.877, f: 206.682, l: "D'", a: 0}],
[{o: 0.18, f: 12.908, l: "B", a: 0},
{o: 12.908, f: 20.832, l: "B", a: 1},
{o: 20.832, f: 32.12, l: "B", a: 0},
{o: 32.12, f: 41.72, l: "F", a: 1},
{o: 41.72, f: 51.26, l: "F", a: 0},
{o: 51.26, f: 60.452, l: "B", a: 1},
{o: 60.452, f: 73.72, l: "B", a: 0},
{o: 73.72, f: 83.448, l: "F", a: 1},
{o: 83.448, f: 89.94, l: "B", a: 0},
{o: 89.94, f: 98.528, l: "B", a: 1},
{o: 98.528, f: 107.32, l: "F", a: 0},
{o: 107.32, f: 116.976, l: "B", a: 1},
{o: 116.976, f: 124.212, l: "F", a: 0},
{o: 124.212, f: 140.172, l: "B", a: 1},
{o: 140.172, f: 150.72, l: "B", a: 0},
{o: 150.72, f: 162.108, l: "B", a: 1},
{o: 162.108, f: 169.444, l: "B", a: 0},
{o: 169.444, f: 180.464, l: "F", a: 1},
{o: 180.464, f: 191.384, l: "F", a: 0},
{o: 191.384, f: 196.368, l: "B", a: 1},
{o: 196.368, f: 204.028, l: "B", a: 0}],
[{o: 0.18, f: 12.908, l: "B", a: 0},
{o: 12.908, f: 20.832, l: "B", a: 1},
{o: 20.832, f: 32.12, l: "A", a: 0},
{o: 32.12, f: 41.72, l: "F", a: 1},
{o: 41.72, f: 51.26, l: "E", a: 0},
{o: 51.26, f: 60.452, l: "A", a: 1},
{o: 60.452, f: 73.72, l: "I", a: 0},
{o: 73.72, f: 83.448, l: "F", a: 1},
{o: 83.448, f: 89.94, l: "J", a: 0},
{o: 89.94, f: 98.528, l: "D", a: 1},
{o: 98.528, f: 107.32, l: "G", a: 0},
{o: 107.32, f: 116.976, l: "B", a: 1},
{o: 116.976, f: 124.212, l: "C", a: 0},
{o: 124.212, f: 140.172, l: "B", a: 1},
{o: 140.172, f: 150.72, l: "B", a: 0},
{o: 150.72, f: 162.108, l: "B", a: 1},
{o: 162.108, f: 169.444, l: "H", a: 0},
{o: 169.444, f: 180.464, l: "C", a: 1},
{o: 180.464, f: 191.384, l: "G", a: 0},
{o: 191.384, f: 196.368, l: "D", a: 1},
{o: 196.368, f: 204.028, l: "H", a: 0}],
[{o: 0.6, f: 8.347, l: "1", a: 0},
{o: 8.347, f: 30.347, l: "5", a: 1},
{o: 30.347, f: 51.04, l: "1", a: 0},
{o: 51.04, f: 55.413, l: "5", a: 1},
{o: 55.413, f: 59.44, l: "1", a: 0},
{o: 59.44, f: 64.667, l: "4", a: 1},
{o: 64.667, f: 72.32, l: "2", a: 0},
{o: 72.32, f: 76.373, l: "4", a: 1},
{o: 76.373, f: 82.453, l: "2", a: 0},
{o: 82.453, f: 88.947, l: "8", a: 1},
{o: 88.947, f: 93.093, l: "6", a: 0},
{o: 93.093, f: 118.4, l: "2", a: 1},
{o: 118.4, f: 124.813, l: "1", a: 0},
{o: 124.813, f: 129.227, l: "2", a: 1},
{o: 129.227, f: 134, l: "4", a: 0},
{o: 134, f: 144.84, l: "3", a: 1},
{o: 144.84, f: 159.893, l: "1", a: 0},
{o: 159.893, f: 170.08, l: "3", a: 1},
{o: 170.08, f: 179.867, l: "1", a: 0},
{o: 179.867, f: 185.933, l: "4", a: 1},
{o: 185.933, f: 191.6, l: "3", a: 0},
{o: 191.6, f: 196.627, l: "6", a: 1},
{o: 196.627, f: 206.547, l: "7", a: 0}],
[{o: 0, f: 17.88, l: "a", a: 0},
{o: 17.88, f: 30.545, l: "a", a: 1},
{o: 30.545, f: 87.165, l: "b", a: 0},
{o: 87.165, f: 139.315, l: "c", a: 1},
{o: 139.315, f: 191.465, l: "c", a: 0},
{o: 191.465, f: 204.875, l: "d", a: 1},
{o: 204.875, f: 206.365, l: "e", a: 0}],
[{o: 0, f: 129.881, l: "n1", a: 0},
{o: 129.881, f: 145.879, l: "A", a: 1},
{o: 145.879, f: 181.708, l: "n2", a: 0},
{o: 181.708, f: 198.078, l: "A", a: 1},
{o: 198.078, f: 206.611, l: "n3", a: 0}],
[{o: 0, f: 0.18, l: "G", a: 0},
{o: 0.18, f: 0.18, l: "B", a: 1},
{o: 0.18, f: 33.36, l: "A", a: 0},
{o: 33.36, f: 57.216, l: "C", a: 1},
{o: 57.216, f: 96.908, l: "A", a: 0},
{o: 96.908, f: 127.836, l: "D", a: 1},
{o: 127.836, f: 151.124, l: "F", a: 0},
{o: 151.124, f: 179.652, l: "D", a: 1},
{o: 179.652, f: 203.956, l: "F", a: 0},
{o: 203.956, f: 206.656, l: "G", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000993.ogg";

var artist = "Compilations";

var track = "The Pearls";
