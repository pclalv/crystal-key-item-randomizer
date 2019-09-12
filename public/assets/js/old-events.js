window.EVENT_ROUTE_30_BATTLE = 20
window.FIRST_TIME = 78; // OlivinePortSailorAfterHOFScript.FirstTime
window.NOP = 0;

window.OldEvents = [
    // EVENT_ROUTE_30_BATTLE is arbitray; we just want a value such
    // that these checks always pass.

    // TODO: try EVENT_GOT_A_POKEMON_FROM_ELM instead
    {
	"name": "CHECK_EVENT_ROUTE_30_YOUNGSTER_JOEY", // skip the "important" battle
	"address": 771120,
	"originalValue": 21,
	"replacementValue": EVENT_ROUTE_30_BATTLE // FIXME: will things break if the player DOES return the MYSTERY EGG to elm?
    },
    {
	"name": "CHECK_EVENT_OLIVINE_PORT_SPRITES_AFTER_HALL_OF_FAME", // travel via SS Aqua without having beat the elite 4
	"address": 771273,
	"originalValue": 56,
	"replacementValue": EVENT_ROUTE_30_BATTLE
    },
    {
	"name": "CHECK_EVENT_RESTORED_POWER_TO_KANTO", // travel via train without completing the power plant sidequest
	"location": "goldenrod",
	"address": 1615905,
	"originalValue": 205,
	"replacementValue": EVENT_ROUTE_30_BATTLE
    },
    {
	"name": "CHECK_EVENT_RESTORED_POWER_TO_KANTO", // travel via train without completing the power plant sidequest
	"location": "saffron",
	"address": 348399,
	"originalValue": 205,
	"replacementValue": EVENT_ROUTE_30_BATTLE
    },

    // travel via SS Aqua on any day
    {
	"name": "IFEQUAL_WEEKDAY_SATURDAY",
	"address": 477504,
	"originalValue": 119, // .NextShipMonday
	"replacementValue": FIRST_TIME
    },
    {
	"name": "IFEQUAL_WEEKDAY_SUNDAY",
	"address": 477500,
	"originalValue": 119, // .NextShipMonday
	"replacementValue": FIRST_TIME
    },
    {
	"name": "IFEQUAL_WEEKDAY_TUESDAY",
	"address": 477508,
	"originalValue": 129, // .NextShipFriday
	"replacementValue": FIRST_TIME
    },
    {
	"name": "IFEQUAL_WEEKDAY_WEDNESDAY",
	"address": 477512,
	"originalValue": 129, // .NextShipFriday
	"replacementValue": FIRST_TIME
    },
    {
	"name": "IFEQUAL_WEEKDAY_THURSDAY",
	"address": 477516,
	"originalValue": 129, // .NextShipFriday
	"replacementValue": FIRST_TIME
    },

    {
	"name": "CHECK_EVENT_RESTORED_POWER_TO_KANTO", // get LOST_ITEM from Pokemon Fan Club guy at any time after fixing the Power Plant
	"location": "vermilion",
	"address": 1644603,
	"originalValue": 207, // EVENT_MET_COPYCAT_FOUND_OUT_ABOUT_LOST_ITEM
	"replacementValue": 201 // EVENT_RESTORED_POWER_TO_KANTO
    },

    {
	"name": "Remove cuttable tree in Ilex Forest", // for more variety :) having HM_CUT always appear within the first four items gotten is quite boring.
	"location": "ilexforest",
	"address": 721295,
	"originalValue": 15, // tree
	"replacementValue": 1 // grass
    },

    // Ensure that the player can get the Flower Shop item even if
    // they've already fought Sudowoodo
    {
	"name": "FlowerShopTeacherScript.CHECKEVENT_EVENT_FOUGHT_SUDOWOODO",
	"address": 349021,
	"originalValue": 49,
	"replacementValue": NOP
    },
    {
	"name": "FlowerShopTeacherScript.CHECKEVENT_EVENT_FOUGHT_SUDOWOODO",
	"address": 349022,
	"originalValue": 42,
	"replacementValue": NOP
    },
    {
	"name": "FlowerShopTeacherScript.CHECKEVENT_EVENT_FOUGHT_SUDOWOODO",
	"address": 349023,
	"originalValue": 0,
	"replacementValue": NOP
    },
    {
	"name": "FlowerShopTeacherScript.CHECKEVENT_EVENT_FOUGHT_SUDOWOODO",
	"address": 349024,
	"originalValue": 9,
	"replacementValue": NOP
    },
    {
	"name": "FlowerShopTeacherScript.CHECKEVENT_EVENT_FOUGHT_SUDOWOODO",
	"address": 349025,
	"originalValue": 143,
	"replacementValue": NOP
    },
    {
	"name": "FlowerShopTeacherScript.CHECKEVENT_EVENT_FOUGHT_SUDOWOODO",
	"address": 349026,
	"originalValue": 83,
	"replacementValue": NOP
    }
];
