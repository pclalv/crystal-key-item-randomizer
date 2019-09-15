"use strict";

window.KeyItems = new Map(Object.entries({
    "BICYCLE": {
        "address": 345957,
        "value": 7
    },
    "SQUIRTBOTTLE": {
        "address": 349058,
        "value": 175
    },
    "BLUE_CARD": {
        "address": 382984,
        "value": 116
    },
    "BASEMENT_KEY": {
        "address": 393277,
        "value": 133
    },
    "CLEAR_BELL": {
        "address": 393435,
        "value": 70
    },
    "OLD_ROD": {
        "address": 433001,
        "value": 58
    },
    "HM_WHIRLPOOL": {
        "address": 446871,
        "value": 248
    },
    "HM_CUT": {
        "address": 454075,
        "value": 243
    },
    "RED_SCALE": {
        "address": 458877,
        "value": 66
    },
    "S_S_TICKET": {
        "address": 495156,
        "value": 68
    },
    "COIN_CASE": {
        "address": 508678,
        "value": 54
    },
    "CARD_KEY": {
        "address": 514508,
        "value": 127
    },
    "HM_WATERFALL": {
        "address": 517234,
        "value": 249
    },
    "SUPER_ROD": {
        "address": 521368,
        "value": 61
    },
    "HM_SURF": {
        "address": 627978,
        "value": 245
    },
    "ITEMFINDER": {
        "address": 632335,
        "value": 55
    },
    "GOOD_ROD": {
        "address": 640815,
        "value": 59
    },
    "HM_STRENGTH": {
        "address": 641230,
        "value": 246
    },
    "SECRETPOTION": {
        "address": 647089,
        "value": 67
    },
    "HM_FLASH": {
        "address": 1591747,
        "value": 247
    },
    "MACHINE_PART": {
        "address": 1606840,
        "value": 128
    },
    "PASS": {
        "address": 1617761,
        "value": 134
    },
    "SILVER_WING": {
        "address": 1622044,
        "value": 71
    },
    "LOST_ITEM": {
        "address": 1644619,
        "value": 130
    },
    "MYSTERY_EGG": {
        "address": 1666670,
        "value": 69
    },
    "HM_FLY": {
        "address": 1704097,
        "value": 244
    }
}));

window.MaxInt32 = 2147483647
window.DefaultSeed = Math.floor(Math.random() * (MaxInt32 + 1));

window.GiveItemLineRanges = {
    "BICYCLE": [346197, 346206],
    "LOST_ITEM": [1645845, 1645853],
    "MYSTERY_EGG": [1667037, 1667050],
    "RED_SCALE": [459756, 459767],
    "SECRETPOTION": [647265, 647279]
}

window.GSCCharacterEncoding = new Map(Object.entries({
    "A": 128,
    "B": 129,
    "C": 130,
    "D": 131,
    "E": 132,
    "F": 133,
    "G": 134,
    "H": 135,
    "I": 136,
    "J": 137,
    "K": 138,
    "L": 139,
    "M": 140,
    "N": 141,
    "O": 142,
    "P": 143,
    "Q": 144,
    "R": 145,
    "S": 146,
    "T": 147,
    "U": 148,
    "V": 149,
    "W": 150,
    "X": 151,
    "Y": 152,
    "Z": 153
}));

function encodeGSCString(str) {
    return str.split("").map(function (chr) {
        return GSCCharacterEncoding.get(chr);
    });
}

function embedDownloadLink(filename, content) {
    var parent = document.getElementById("download");
    var element = document.createElement("a");
    var blob = new Blob([content]);

    element.innerHTML = "download randomized ROM";
    element.className = "btn btn-default btn-file";
    element.href = window.URL.createObjectURL(blob);
    element.download = filename;

    parent.appendChild(element);
}

function hideUploadInput() {
   document.getElementById("upload").style.display = "none";
}

function applyDiffs(rom) {
    for (var diff of Diffs) {
	applyDiff(rom, diff);
    }
}

function applyDiff(rom, diff) {
    var idxs = R.range(diff.address_range.begin, diff.address_range.end);

    if (idxs.length != diff.integer_values.old.length || idxs.length != diff.integer_values.new.length) {
	console.log("Found mismatched lengths while applying " + diff.name);
    }

    console.log("applying " + diff.name)
    for (var ii in R.range(0, idxs.length)) {
	var romIdx = idxs[ii];

	if (diff.integer_values.old[ii] != rom[romIdx]) {
	    console.log("Found unexpected value while applying " + diff.name);
	}

	console.log(diff.integer_values.old[ii] + " -> " + diff.integer_values.new[ii])
	rom[romIdx] = diff.integer_values.new[ii];
    }
}

function applySwap(rom, swap) {
    var original = KeyItems.get(swap[0]);
    var replacement = KeyItems.get(swap[1]);
    rom[original.address] = replacement.value;

    // if (isGiveItem(swap[0])) {
    //     // update giveItem text...
    //     encodeGSCString(swap[0])
    // }
}

function applySwaps(rom, swaps) {
    for (var swap of swaps) {
        applySwap(rom, swap);
    }
}

function patchRom(rom, swaps, seed) {
    hideUploadInput();
    applySwaps(rom, swaps);
    applyDiffs(rom);
    embedDownloadLink("pokecrystal-key-item-randomized-seed-" + seed + ".gbc", rom);
}

function requestSwapsAndPatchRomFile(event) {
    var reader = event.target;
    var romBytes = new Uint8Array(reader.result);
    var seed = document.getElementById("seed").value;
    var swaps = new Request("/seed?seed-id=" + seed);

    fetch(swaps).then(
        (swapsResponse) => swapsResponse.json(),
        console.error
    ).then(
        (swapsJson) => new Map(Object.entries(swapsJson.swaps)),
        console.error
    ).then(
        (swaps) => patchRom(romBytes, swaps, seed),
        console.error
    );
}

function handleFiles() {
    var romFile = this.files[0];
    var fileReader = new FileReader();

    fileReader.onload = requestSwapsAndPatchRomFile;
    fileReader.readAsArrayBuffer(romFile);
}

document
    .getElementById("rom-file")
    .addEventListener("change", handleFiles, false);

document.getElementById("seed").value = DefaultSeed;
