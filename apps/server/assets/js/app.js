"use strict";

// Brunch automatically concatenates all files in your
// watched paths. Those paths can be configured at
// config.paths.watched in "brunch-config.js".
//
// However, those files will only be executed if
// explicitly imported. The only exception are files
// in vendor, which are never wrapped in imports and
// therefore are always executed.

// Import dependencies
//
// If you no longer want to use a dependency, remember
// to also remove its path from "config.paths.watched".
import "phoenix_html";

// Import local files
//
// Local files can be imported directly using relative
// paths "./socket" or full ones "web/static/js/socket".

// import socket from "./socket"

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

function embedDownloadLink(filename, content) {
    var parent = document.getElementById("upload-download-row");
    var element = document.createElement("a");
    var blob = new Blob([content]);

    element.innerHTML = "download randomized ROM";
    element.className = "btn btn-default btn-file";
    element.href = window.URL.createObjectURL(blob);
    element.download = filename;

    parent.appendChild(element);
}

function hideUploadInput() {
   document.getElementById("rom-file").parentElement.style.display = "none";
}

function applySwap(swap, rom) {
    var original = KeyItems.get(swap[0]);
    var replacement = KeyItems.get(swap[1]);
    rom[original.address] = replacement.value;
}

function applySwaps(swaps, rom) {
    for (var swap of swaps) {
        applySwap(swap, rom);
    }

    hideUploadInput();
    embedDownloadLink("pokecrystal-key-item-randomized.gbc", rom);
}

function requestSwapsAndPatchRomFile(event) {
    var reader = event.target;
    var romBytes = new Uint8Array(reader.result);
    var swaps = new Request("/api/swaps");

    fetch(swaps).then(
        (swapsResponse) => swapsResponse.json(),
        console.log
    ).then(
        (swapsJson) => new Map(Object.entries(swapsJson.swaps)),
        console.error
    ).then(
        (swaps) => applySwaps(swaps, romBytes),
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
