// This file contains data that the frontend can use to modify the ROM
// file with changes beyond the usual key item swaps.

window.Diffs = [
    // Disable the important battle
    {
        "integer_values": {
            "old": [ 39, 30, 9, 7, 0, 255, 255, 144, 0, 115, 86, 20, 7 ],
            "new": [ 39, 30, 9, 7, 0, 255, 255, 144, 0, 115, 86, 26, 0 ]
        },
        "address_range": {
            "begin": 1711229,
            "end": 1711242
        },
        "name": "Route30_MapEvents.ckir_BEFORE_OBJECT_EVENT_YOUNGSTER_EVENT_ROUTE_30_BATTLE"
    },
    {
        "integer_values": {
            "old": [ 76, 28, 9, 6, 0, 255, 255, 128, 0, 239, 38, 20, 7 ],
            "new": [ 76, 28, 9, 6, 0, 255, 255, 128, 0, 239, 38, 26, 0 ]
        },
        "address_range": {
            "begin": 1711294,
            "end": 1711307
        },
        "name": "Route30_MapEvents.ckir_BEFORE_OBJECT_EVENT_MONSTER_EVENT_ROUTE_30_BATTLE_0"
    },
    {
        "integer_values": {
            "old": [ 76, 29, 9, 7, 0, 255, 255, 144, 0, 239, 38, 20, 7 ],
            "new": [ 76, 29, 9, 7, 0, 255, 255, 144, 0, 239, 38, 26, 0 ]
        },
        "address_range": {
            "begin": 1711307,
            "end": 1711320
        },
        "name": "Route30_MapEvents.ckir_BEFORE_OBJECT_EVENT_MONSTER_EVENT_ROUTE_30_BATTLE_1"
    },
    // appear the real youngster joey
    {
        "integer_values": {
            "old": [ 39, 32, 6, 9, 0, 255, 255, 146, 3, 154, 86, 21, 7 ],
            "new": [ 39, 32, 6, 9, 0, 255, 255, 146, 3, 154, 86, 200, 6 ]
        },
        "address_range": {
            "begin": 1711242,
            "end": 1711255
        },
        "name": "Route30_MapEvents.ckir_BEFORE_EVENT_ROUTE_30_YOUNGSTER_JOEY"
    },

    // ensure that the player can still get the Flower Shop item even
    // if they've already defeated/caught the Sudowoodo
    {
        "integer_values": {
            "old": [ 49,  42, 0 ],
            "new": [ 49, 200, 6 ]
        },
        "address_range": {
            "begin": 349021,
            "end": 349024
        },
        "name": "FlowerShopTeacherScript.ckir_BEFORE_CHECKEVENT_EVENT_FOUGHT_SUDOWOODO"
    },
    // allow the player to skip talking to Floria back at the Flower Shop
    {
        "integer_values": {
            "old": [ 49, 186, 0 ],
            "new": [ 49,  26, 0 ]
        },
        "address_range": {
            "begin": 349039,
            "end": 349042
        },
        "name": "FlowerShopTeacherScript.ckir_BEFORE_CHECKEVENT_EVENT_TALKED_TO_FLORIA_AT_FLOWER_SHOP"
    },

    // Ensure early transport to Saffron
    {
        "integer_values": {
            "old": [ 49, 205, 0 ],
            "new": [ 49,  26, 0 ]
        },
        "address_range": {
            "begin": 348398,
            "end": 348401
        },
        "name": "GoldenrodMagnetTrainStationOfficerScript.ckir_BEFORE_CHECKEVENT_EVENT_RESTORED_POWER_TO_KANTO"
    },

    // allow player to ride SS Aqua on any day
    {
        "integer_values": {
            "old": [ 6, 6, 242, 73 ],
            "new": [ 6, 6, 192, 73 ]
        },
        "address_range": {
	    "begin": 477616,
            "end": 477620
        },
        "name": "OlivinePortSailorAfterHOFScript.ckir_BEFORE_IFEQUAL_WEEKDAY_SATURDAY"
    },
    {
        "integer_values": {
            "old": [ 6, 3, 248, 73 ],
            "new": [ 6, 3, 192, 73 ]
        },
        "address_range": {
            "begin": 477624,
            "end": 477628
        },
        "name": "OlivinePortSailorAfterHOFScript.ckir_BEFORE_IFEQUAL_WEEKDAY_WEDNESDAY"
    },
    {
        "integer_values": {
            "old": [ 6, 0, 242, 73 ],
            "new": [ 6, 0, 192, 73 ]
        },
        "address_range": {
            "begin": 477612,
            "end": 477616
        },
        "name": "OlivinePortSailorAfterHOFScript.ckir_BEFORE_IFEQUAL_WEEKDAY_SUNDAY"
    },
    {
        "integer_values": {
            "old": [ 6, 4, 248, 73 ],
            "new": [ 6, 4, 192, 73 ]
        },
        "address_range": {
            "begin": 477628,
            "end": 477632
        },
        "name": "OlivinePortSailorAfterHOFScript.ckir_BEFORE_IFEQUAL_WEEKDAY_THURSDAY"
    },
    {
        "integer_values": {
            "old": [ 6, 2, 248, 73 ],
            "new": [ 6, 2, 192, 73 ]
        },
        "address_range": {
            "begin": 477620,
            "end": 477624
        },
        "name": "OlivinePortSailorAfterHOFScript.ckir_BEFORE_IFEQUAL_WEEKDAY_TUESDAY"
    },

    // make Olivine Port always appear as if in its post-Hall-of-Fame
    // state
    {
        "integer_values": {
            "old": [ 73, 19, 11, 7, 0, 255, 255, 0, 0, 254, 73, 55, 7 ],
            "new": [ 73, 19, 11, 7, 0, 255, 255, 0, 0, 254, 73, 26, 0 ]
        },
        "address_range": {
            "begin": 478530,
            "end": 478543
        },
        "name": "OlivinePort_MapEvents.ckir_BEFORE_OBJECT_EVENT_EVENT_OLIVINE_PORT_SPRITES_BEFORE_HALL_OF_FAME_0"
    },
    {
        "integer_values": {
            "old": [ 59, 18, 8, 7, 0, 255, 255, 0, 0, 1, 74, 55, 7 ],
            "new": [ 59, 18, 8, 7, 0, 255, 255, 0, 0, 1, 74, 26, 0 ]
        },
        "address_range": {
            "begin": 478556,
            "end": 478569
        },
        "name": "OlivinePort_MapEvents.ckir_BEFORE_OBJECT_EVENT_EVENT_OLIVINE_PORT_SPRITES_BEFORE_HALL_OF_FAME_1"
    },
    {
        "integer_values": {
            "old": [ 59, 18, 17, 7, 0, 255, 255, 0, 0, 12, 74, 55, 7 ],
            "new": [ 59, 18, 17, 7, 0, 255, 255, 0, 0, 12, 74, 26, 0 ]
        },
        "address_range": {
            "begin": 478569,
            "end": 478582
        },
        "name": "OlivinePort_MapEvents.ckir_BEFORE_OBJECT_EVENT_EVENT_OLIVINE_PORT_SPRITES_BEFORE_HALL_OF_FAME_2"
    },
    {
        "integer_values": {
            "old": [ 36, 19, 15, 6, 0, 255, 255, 0, 0, 34, 74,  56, 7 ],
            "new": [ 36, 19, 15, 6, 0, 255, 255, 0, 0, 34, 74, 200, 6 ]
        },
        "address_range": {
            "begin": 478595,
            "end": 478608
        },
        "name": "OlivinePort_MapEvents.ckir_BEFORE_OBJECT_EVENT_EVENT_OLIVINE_PORT_SPRITES_BEFORE_HALL_OF_FAME_3"
    },
    {
        "integer_values": {
            "old": [ 73, 19, 10, 9, 0, 255, 255, 0, 0, 156, 73, 56, 7 ],
            "new": [ 73, 19, 10, 9, 0, 255, 255, 0, 0, 156, 73, 200, 6 ]
        },
        "address_range": {
            "begin": 478543,
            "end": 478556
        },
        "name": "OlivinePort_MapEvents.ckir_BEFORE_OBJECT_EVENT_EVENT_OLIVINE_PORT_SPRITES_AFTER_HALL_OF_FAME_0"
    },
    {
        "integer_values": {
            "old": [ 39, 19, 8, 6, 0, 255, 255, 0, 0, 23, 74, 56, 7 ],
	    "new": [ 39, 19, 8, 6, 0, 255, 255, 0, 0, 23, 74, 200, 6 ]
        },
        "address_range": {
            "begin": 478582,
            "end": 478595
        },
        "name": "OlivinePort_MapEvents.ckir_BEFORE_OBJECT_EVENT_EVENT_OLIVINE_PORT_SPRITES_AFTER_HALL_OF_FAME_1"
    },

    // ensure Pokemon Fan Club item is obtainable
    {
        "integer_values": {
            "old": [ 49, 207, 0 ],
            "new": [ 49, 205, 0 ]
        },
        "address_range": {
            "begin": 1644603,
            "end": 1644606
        },
        "name": "PokemonFanClubClefairyGuyScript.ckir_BEFORE_CHECKEVENT_EVENT_MET_COPYCAT_FOUND_OUT_ABOUT_LOST_ITEM"
    },

    // ensure Cerulean Gym item is obtainable
    {
        "integer_values": {
            "old": [ 49, 201, 0 ],
            "new": [ 49, 200, 6 ]
        },
        "address_range": {
            "begin": 1609319,
            "end": 1609322
        },
        "name": "PowerPlantManager.ckir_BEFORE_CHECKEVENT_EVENT_RETURNED_MACHINE_PART"
    },

    // permit early transport to Goldenrod
    {
        "integer_values": {
            "old": [ 49, 205, 0 ],
            "new": [ 49,  26, 0 ]
        },
        "address_range": {
            "begin": 1615904,
            "end": 1615907
        },
        "name": "SaffronMagnetTrainStationOfficerScript.ckir_BEFORE_CHECKEVENT_EVENT_RESTORED_POWER_TO_KANTO"
    },

    // for more variety :) having HM_CUT always appear within the first four items gotten is quite boring.
    {
	"name": "Remove cuttable tree in Ilex Forest",
	"address_range": {
	    "begin": 721295,
	    "end": 721296
	},
	"integer_values": {
	    "old": [15], // tree
	    "new": [ 1]  // grass
	}
    }
]
