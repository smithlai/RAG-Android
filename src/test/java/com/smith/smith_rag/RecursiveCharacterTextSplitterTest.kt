package com.smith.smith_rag

import com.smith.smith_rag.splitters.RecursiveCharacterTextSplitter
import com.smith.smith_rag.splitters.WhiteSpaceSplitter
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RecursiveCharacterTextSplitterTest {
    @get:Rule
    val testName = TestName()

    @Before
    fun setup(){
        println("vvvvvvvv Running test: ${testName.methodName} vvvvvvvv")
    }
    @After
    fun clear(){
        println("^^^^^^^^  ${testName.methodName} End ^^^^^^^^")
    }
    @Test
    fun `**001** test simple split`() {
        val chunkSize = 30
        val chunkOverlap = 10
        val splitter = RecursiveCharacterTextSplitter(chunkSize = chunkSize, chunkOverlap = chunkOverlap)
//        val splitter = WhiteSpaceSplitter(chunkSize = 20, chunkOverlap = 10)
        val text = "Hello world! This is a test. Hello\n\nWorld\nThis\nis\na\ntest."
        println("aaaaa" + text)
        val chunks = splitter.splitText(text)
        chunks.forEachIndexed { index, s ->
            println("Chunks $index(${s.length}): [$s]")
        }

        assertTrue(chunks.all { it.length <= chunkSize+chunkOverlap }) // 確保不超過 chunkSize
    }

    @Test
    fun `**002** test splitting with newlines`() {
        val chunkSize = 20
        val chunkOverlap = 10
        val splitter = RecursiveCharacterTextSplitter(chunkSize = chunkSize, chunkOverlap = chunkOverlap)
        val text = "Hello\n\nWorld\nThis\nis\na\ntest."
        val chunks = splitter.splitText(text)

        chunks.forEachIndexed { index, s ->
            println("Chunks $index(${s.length}): [$s]")
        }
        assertTrue(chunks.all { it.length <= chunkSize + chunkOverlap }) // 確保每個 chunk 不超過 10
    }

    @Test
    fun `**003** test long word splitting`() {
        val chunkSize = 10
        val chunkOverlap = 2
        val splitter = RecursiveCharacterTextSplitter(chunkSize = chunkSize, chunkOverlap = chunkOverlap)
        val text = "Supercalifragilisticexpialidocious"
        val chunks = splitter.splitText(text)

        chunks.forEachIndexed { index, s ->
            println("Chunks $index(${s.length}): [$s]")
        }
        assertTrue(chunks.all { it.length <= chunkSize+chunkOverlap }) // 確保長字串被切割
    }

    @Test
    fun `**004** test long paragraph`() {
        val chunkSize = 256
        val chunkOverlap = 32
        val splitter = RecursiveCharacterTextSplitter(chunkSize = chunkSize, chunkOverlap = chunkOverlap)
        val text = "PRODUCT SPEC SHEET\n" +
                "TC73/TC78 MOBILE COMPUTERS\n" +
                "TC73/TC78 Mobile Computers\n" +
                "The ultra-rugged mobile computer re-imagined for the new age of\n" +
                "mobility\n" +
                "Introducing the TC73/TC78, a new generation of mobile computers designed for a new generation of mobile solutions. Streaming\n" +
                "video, video calls, intuitive multimedia-rich apps, augmented reality apps that enable new, more effective workflows — the rugged,\n" +
                "thinner and lighter TC73/TC78 supports them all. More processing power for simultaneous app support. A 6-inch advanced display.\n" +
                "More flexible battery options. Comprehensive wireless network support for unmatched performance and reliability — Wi-Fiᵀᴹ 6E, 5G\n" +
                "and CBRS¹. Bluetooth 5.2 for superior accessory performance. The next level in scanning performance with the SE55 Advanced Range\n" +
                "scan engine. Crystal clear voice on audio and video calls. Add the functionality of a workstation, two-way radio, RFID, PBX handset\n" +
                "and an integrated mobile dimensioning solution. Mobility DNAᵀᴹ tools simplify every aspect of the TC73/TC78. And you get five years\n" +
                "of product availability with 10 years of service and support. Step up to the TC73/TC78 — and step into the new world of mobility.\n" +
                "More rugged, lighter, thinner — and more ergonomic\n" +
                "Advanced manufacturing and materials technologies enable a\n" +
                "more rugged design — without adding weight or thickness.\n" +
                "It’s built to handle 10 ft./3.05 m drops to concrete and 2,000\n" +
                "consecutive tumbles. With IP65 and IP68 sealing, it’s dustproof\n" +
                "and waterproof — submerge it in water and spray it down\n" +
                "with a hose. Corning Gorilla Glass protects the display and\n" +
                "imager window with maximum scratch-resistance and\n" +
                "shatter-proofing — two of the most crucial and vulnerable\n" +
                "features. The advanced industrial design and ergonomics\n" +
                "bring superior comfort to practically any size hand, minimizing\n" +
                "the effort required to hold and use these devices through\n" +
                "exceptional balance, a grip area that is nearly 20% thinner and\n" +
                "proper key placement and size.\n" +
                "Loaded with all the latest technologies\n" +
                "Industry-leading 6-inch advanced display\n" +
                "More display space means less scrolling. The edge-to-edge\n" +
                "Blazing speed\n" +
                "display is 28% larger³, easy to view indoors and outside, works\n" +
                "Run simultaneous demanding applications with virtually\n" +
                "when wet and with a stylus or a finger — even with a glove.\n" +
                "instant response times with class-leading processing power,\n" +
                "memory and storage. The next generation Qualcomm 6490\n" +
                "All the right battery features and flexibility to stay powered\n" +
                "octa-core processor delivers 90% more processing power, a\n" +
                "up\n" +
                "400% faster graphics processor and locationing that is six\n" +
                "Choose from four battery options: standard, extended\n" +
                "times more accurate.² And up to 8 GB of RAM and 128 GB\n" +
                "capacity, wireless charge⁴ and BLE, enabling device location\n" +
                "Flash, plus a 2 TB MicroSD card slot provides ample storage\n" +
                "with Device Tracker — even if the battery is depleted. All\n" +
                "for the most data-intensive applications.\n" +
                "batteries are fast charging. The sealed battery compartment\n" +
                "enables anywhere, anytime battery changes. Charge it with\n" +
                "any standard USB-C charging cable. And PowerPrecision+\n" +
                "battery metrics make it easy to identify and remove unhealthy\n" +
                "batteries from your battery pool.\n" +
                "\n" +
                "Step up to the TC73/TC78 and step into the new world of mobility.\n" +
                "For more information, please visit www.zebra.com/tc73-tc78\n" +
                "All the latest wireless connections Turn your TC73/TC78 into a workstation with Workstation\n" +
                "Connect software and the Workstation Connect cradle. Add a\n" +
                "Give your workers the fastest speeds with Wi-Fi 6E, 5G and\n" +
                "lightning fast RFID sled, capable of reading an impressive 1300\n" +
                "CBRS. Wi-Fi 6E is three times faster and supports four times\n" +
                "tags per second. And turn the TC73/TC78 into a two-way radio\n" +
                "the number of devices, all with less power. 5G brings wired\n" +
                "that works over Wi-Fi or cellular and a fully-featured PBX\n" +
                "speeds to wireless networks, plus better signal quality and\n" +
                "handset with a custom interface that makes it easy to execute\n" +
                "strength. And support for CBRS private LTE networks⁴ enables\n" +
                "even the most complex telephony functions.\n" +
                "cost-effective wireless connectivity in the largest indoor and\n" +
                "outdoor facilities.\n" +
                "Drive workforce productivity and device\n" +
                "Superior flexibility with Bluetooth 5.2\n" +
                "value to a new level with Mobility DNA\n" +
                "Better audio quality improves the experience for callers on\n" +
                "both ends of a call. Lower power requirements extend battery\n" +
                "Simplify device integration\n" +
                "cycle times. And enable unique business collaboration\n" +
                "Create rich apps that take full advantage of your device’s\n" +
                "solutions — connect one Bluetooth device to multiple host\n" +
                "capabilities with Enterprise Mobility Management Tool Kit.\n" +
                "devices simultaneously or connect multiple Bluetooth devices\n" +
                "Build feature-rich web applications with Enterprise Browser.\n" +
                "to one host.\n" +
                "And easily capture and integrate barcode data into your apps\n" +
                "without writing any code with DataWedge API.\n" +
                "High performance scanning for every job\n" +
                "Choose from two scan engines. The SE4770 offers a standard\n" +
                "Increase device security\n" +
                "24 in./60 cm scanning range, a wide field of view and\n" +
                "Keep your devices secure every day they are in service with\n" +
                "maximum motion tolerance. The SE55 1D/2D Advanced Range\n" +
                "LifeGuardᵀᴹ for Androidᵀᴹ. And restrict apps and device\n" +
                "scan engine with IntelliFocusᵀᴹ technology enables the\n" +
                "features with Enterprise Home Screen.\n" +
                "capture of barcodes in hand and as far as 40 ft./12.2 m away⁵\n" +
                "— without bending down or climbing ladders. The scan button\n" +
                "Deploy devices quickly and easily\n" +
                "is rated for one million actuations. Both scanners capture\n" +
                "Let your devices provision themselves right out of the box with\n" +
                "barcodes in virtually any condition and lighting. And with\n" +
                "Zebra Zero-Touch. Stage a handful or thousands of devices\n" +
                "Mobility DNA’s Simulscan NG, capture all of the barcodes you\n" +
                "with a scan of a barcode or tap on an NFC tag with StageNow.\n" +
                "need on an item or package with one press of the scan button\n" +
                "— and capture and process information on documents,\n" +
                "Make device management simple and easy\n" +
                "including signatures and barcodes.\n" +
                "Locate missing devices quickly and easily with Device Tracker.\n" +
                "Capture ultra-high resolution photos and video\n" +
                "Get dependably superior Wi-Fi connections with Wireless\n" +
                "intelligence\n" +
                "Fusion. Control which Google Mobile Services are active on\n" +
                "your devices with GMS Restricted Mode. Eliminate\n" +
                "The 16 MP integrated camera offers the highest resolution in\n" +
                "unnecessary trips to the repair depot with Device Diagnostics\n" +
                "its class. The flash LED generates balanced white light. High\n" +
                "and RxLogger. Enable your EMM to support even the newest\n" +
                "Dynamic Range (HDR) captures details even in the lightest and\n" +
                "features with OEMConfig. And add features to give Android\n" +
                "darkest areas of the photo. And with optional Optical Image\n" +
                "the enterprise-class functionality you need with Mobility\n" +
                "Stabilization (OIS), it’s easy to capture sharp, detailed\n" +
                "Extensions (Mx).\n" +
                "photographs to document proof of condition, proof of delivery\n" +
                "and more.\n" +
                "Increase on-the-job productivity\n" +
                "Hear every word on every call\n" +
                "Easily manage Bluetooth accessories with Device Central.\n" +
                "Make data entry as easy as possible — give workers a soft\n" +
                "Get superior audio quality with three integrated microphones\n" +
                "keypad designed for your data with Enterprise Keyboard.\n" +
                "with noise cancellation, two speakers for loudness, a high\n" +
                "Capture standardized data with OCR Wedge — such as drivers\n" +
                "quality speakerphone and HD Voice, including\n" +
                "licenses and license plates. And convert ‘green’ screens to\n" +
                "Super-wideband (SWB), Wideband (WB) and Fullband (FB). And\n" +
                "intuitive all-touch modern screens with All-touch Terminal\n" +
                "the TC73 and TC78 support wired and Bluetooth wireless\n" +
                "Emulation.\n" +
                "headsets.\n" +
                "Add the functionality of additional devices\n" +
                "Calculate accurate parcel dimensions and shipping charges in\n" +
                "seconds with Zebra Dimensioningᵀᴹ Certified Mobile Parcel, an\n" +
                "industry-first handheld ‘legal for trade’ solution that utilizes\n" +
                "the integrated Time of Flight sensor.\n" +
                "Specifications\n" +
                "Markets and\n" +
                "Applications\n" +
                "Physical Characteristics Wireless LAN\n" +
                "Transportation and Logistics\n" +
                "Dimensions 6.96 in. L x 3.38 in. W x 1.12 in. H/176.8 mm L Radio IEEE 802.11 a/b/g/n/ac/d/h/i/r/k/v/w/mc/ax;\n" +
                "    x 85.8 mm W x 28.4 mm H; Grip area: 3.179 in. 2x2 MU-MIMO; Wi-Fi 6E (802.11ax); Wi-Fi™\n" +
                "• Parcel Delivery\n" +
                "W x 0.80 in. H/80.75 mm W x 20.3 mm H certified; Wi-Fi™ 6E Certified; Dual Band\n" +
                "• Direct Store Delivery (DSD)\n" +
                "Simultaneous; IPv4, IPv6\n" +
                "• Route Accounting\n" +
                "Weight 12.3 oz./349 g with standard battery\n" +
                "• Airlines (under wing)\n" +
                "Data Rates 5 GHz: 802.11a/n/ac/ax — 20 MHz, 40 MHz, 80\n" +
                "MHz, 160 MHz — up to 2402 Mbps; 2.4 GHz:\n" +
                "Display 6.0 inch Full High Definition+ (1080 X 2160);\n" +
                "Field Operations\n" +
                "802.11b/g/n/ax — 20 MHz up to 286.8 Mbps\n" +
                "LED backlight; optically bonded to touch\n" +
                "6 GHz: 802.11ax — 20 MHz, 40 MHz, 80 MHz,\n" +
                "panel\n" +
                "160 MHz — up to 2402 Mbps\n" +
                "• Field mobility\n" +
                "• field service\n" +
                "Imager Window Corning® Gorilla® Glass\n" +
                "Operating Channel 1-13 (2401-2483 MHz): 1, 2, 3, 4, 5, 6,\n" +
                "Channels 7, 8, 9, 10, 11, 12, 13; Channel 36-165\n" +
                "Retail\n" +
                "Touch Panel Multi mode capacitive touch with bare or\n" +
                "(5150-5850 MHz): 36, 40, 44, 48, 52, 56, 60, 64,\n" +
                "gloved fingertip input or conductive stylus\n" +
                "100, 104, 108, 112, 116, 120, 124, 128, 132,\n" +
                "(sold separately); Corning® Gorilla® Glass; • Retail distribution centers\n" +
                "136, 140, 144, 149, 153, 157, 161, 165;\n" +
                "Water droplet rejection; fingerprint resistant\n" +
                "Channel 1-233 (5925-7125 MHz); Channel\n" +
                "anti-smudge coating\n" +
                "Bandwidth: 20/40/80/160 MHz; Actual\n" +
                "operating channels/frequencies and\n" +
                "Power Rechargeable Li-Ion, PowerPrecision+ for bandwidths depend on regulatory rules and\n" +
                "real-time battery metrics; Standard Capacity: certification agency.\n" +
                "4680 mAh (18.01 Watt hours); Extended\n" +
                "Capacity: 7000 mAh (26.95 Watt hours); BLE\n" +
                "Security and WEP (40 or 104 bit); WPA/WPA2 Personal\n" +
                "battery: 4680 mAh; Qi compatible Wireless\n" +
                "Encryption (TKIP, and AES); WPA3 Personal (SAE);\n" +
                "charge battery: 4680 mAh; fast charging,\n" +
                "WPA/WPA2 Enterprise (TKIP and AES); WPA3\n" +
                "Warm Swap battery mode (standard SKUs);\n" +
                "Enterprise (AES) — EAP-TTLS (PAP, MSCHAP,\n" +
                "Hot Swap battery mode (premium SKUs)\n" +
                "MSCHAPv2), EAP-TLS, PEAPv0-MSCHAPv2,\n" +
                "PEAPv1-EAP-GTC, LEAP, EAP-PWD; TC78\n" +
                "Expansion Slot User accessible MicroSD card supports up to WWAN models only — EAP-SIM, EAP-AKA;\n" +
                "2 TB WPA3 Enterprise 192-bit mode (GCMP256) -\n" +
                "EAP-TLS; Enhanced Open (OWE)\n" +
                "SIM TC78 only: 1 Nano SIM and 1 eSIM\n" +
                "Certifications Wi-Fi Alliance Certifications: Wi-Fi CERTIFIED\n" +
                "n; Wi-Fi CERTIFIED ac; Wi-Fi CERTIFIED 6; Wi-Fi\n" +
                "Network TC73: WLAN, WPAN (Bluetooth); TC78: WWAN\n" +
                "Enhanced Open; WPA2-Personal;\n" +
                "Connections 5G\n" +
                "WPA2-Enterprise; WPA3-Personal;\n" +
                "WPA3-Enterprise (includes 192-bit mode);\n" +
                "Notifications Audible tone; multi-color LEDs; haptic\n" +
                "Protected Management Frames; Wi-Fi Agile\n" +
                "feedback\n" +
                "Multiband; WMM; WMM-Power Save;\n" +
                "WMM-Admission Control; Voice-Enterprise;\n" +
                "Keypad On-screen keypad and Zebra enterprise Wi-Fi Direct; QoS Management; OCE\n" +
                "keyboard\n" +
                "Fast Roam PMKID caching; Cisco CCKM; 802.11r; OKC\n" +
                "Voice and Audio Three microphones with noise cancellation;\n" +
                "vibrate alert; dual speakers for loudness;\n" +
                "Bluetooth wireless headset support; high\n" +
                "quality speaker phone; PTT headset (Zebra\n" +
                "Wireless PAN\n" +
                "USB-C) support; cellular circuit switch voice;\n" +
                "HD Voice, including Super-wideband (SWB);\n" +
                "Bluetooth Class 2, Bluetooth v5.2 and Secondary BLE for\n" +
                "Wideband (WB) and Fullband (FB)\n" +
                "    beaconing within BLE battery\n" +
                "Buttons Programmable buttons for maximum\n" +
                "flexibility: dual dedicated scan buttons;\n" +
                "dedicated push-to-talk button; volume\n" +
                "Warranty\n" +
                "up/down buttons and Trigger button of\n" +
                "Trigger Handle via Back I/O\n" +
                "Subject to terms of Zebra’s hardware warranty statement, the\n" +
                "TC73/TC78 is warranted against defects in workmanship and\n" +
                "Interface Ports USB 2.0 (Back I/O — Host Only), USB 3.0\n" +
                "materials for a period of 1 (one) year from the date of shipment.\n" +
                "(Bottom Type C) — Super Speed (Host and\n" +
                "For complete warranty statement, please visit:\n" +
                "Client)\n" +
                "www.zebra.com/warranty\n" +
                "Performance Characteristics\n" +
                "Environmental Compliance\n" +
                "CPU Qualcomm 6490 octa-core, 2.7 GHz\n" +
                "RoHS Directive 2011/65/EU RoHS Amendment (EU) 2015/863 (EN\n" +
                "IEC 63000:2018 Standard); For a complete list of product and\n" +
                "Operating Upgradeable to Android 16 materials compliance, please visit\n" +
                "System\n" +
                "www.zebra.com/environment\n" +
                "Memory 4 GB RAM/64 GB UFS Flash; 8 GB RAM/128 GB\n" +
                "UFS Flash\n" +
                "Recommended Services\n" +
                "Security FIPS 140-2 cryptography can be enabled on\n" +
                "select configurations through a custom SKU Zebra OneCare™ Essential and Select support services; Zebra\n" +
                "Visibility Services — VisibilityIQ™ Foresight. For information on\n" +
                "request. Supports Secure Boot and Verified\n" +
                "Boot Zebra services, please visit\n" +
                "www.zebra.com/services\n" +
                "\n" +
                "User Environment Footnotes\n" +
                "Operating -4° F to 122° F/-20° C to 50° C 1. CBRS is available in the US only.\n" +
                "Temperature 2. Compared to the Qualcomm SD660 processor.\n" +
                "3. Compared to prior generations of the TC7X family.\n" +
                "4. Available in the TC78 only.\n" +
                "Storage -40° F to 158° F/-40° C to 70° C\n" +
                "5. Distance dependent on symbology type and size.\n" +
                "Temperature\n" +
                "Humidity 5% to 95% non-condensing\n" +
                "Mobility DNA\n" +
                "Drop Multiple 10 ft./3.05 m drops to concrete at\n" +
                "Specification room temp per MIL-STD 810H\n" +
                "For more information on Mobility DNA, please visit\n" +
                "Multiple 8 ft./2.4 m drops to concrete over\n" +
                "www.zebra.com/mobilitydna\n" +
                "operating temp -20° C to 50° C per MIL-STD\n" +
                "Mobility DNA features may vary by model and a Support Contract\n" +
                "810H\n" +
                "may be required. To learn what solutions are supported, please\n" +
                "visit:\n" +
                "Tumble 2000 3.3 ft./1.0 m tumbles, meets or exceeds\n" +
                "https://developer.zebra.com/mobilitydna\n" +
                "Specification IEC tumble specification\n" +
                "Sealing IP65 and IP68 with battery per applicable IEC\n" +
                "sealing specifications\n" +
                "Vibration 4 g’s PK Sine (5 Hz to 2 kHz); 0.04 g 2/Hz\n" +
                "Random (20 Hz to 2 kHz); 60 minute duration\n" +
                "per axis, 3 axis\n" +
                "Electrostatic +/- 15 kVdc air discharge; +/- 8 kVdc direct\n" +
                "Discharge (ESD) discharge; +/- 8 kVdc indirect discharge\n" +
                "Automated Interactive Sensor Technology\n" +
                "(IST)\n" +
                "Light Sensor Adjusts display backlight brightness\n" +
                "Magnetometer Detects direction and orientation\n" +
                "Motion Sensor 3-axis accelerometer with MEMS Gyro\n" +
                "Pressure Sensor Detects altitude information for locating\n" +
                "Proximity Detects when the user places the handset\n" +
                "Sensor against head during a phone call to disable\n" +
                "display output and touch input\n" +
                "Gyro Senses linear orientation\n" +
                "General Certifications\n" +
                "TAA compliant; ARCore Google certification sustained\n" +
                "Data Capture\n" +
                "Scanning SE55 1D/2D Advanced Range Scan Engine\n" +
                "with IntelliFocus™ technology; SE4770 1D/2D\n" +
                "Scan Engine\n" +
                "Camera Front — 8 MP; Rear — 16 MP autofocus; flash\n" +
                "LED generates balanced white light; supports\n" +
                "Torch mode + HDR (standard); Optical Image\n" +
                "Stabilization (OIS) (premium SKUs only); Time\n" +
                "of Flight (ToF) Sensor (premium SKUs only)\n" +
                "NFC ISO 14443 Type A and B; FeliCa and ISO 15693\n" +
                "cards; Card Emulation via Host; Contactless\n" +
                "payment support; ECP1.0 and ECP2.0 polling\n" +
                "support; Apple VAS certified; NFC Forum\n" +
                "Certified and Google Smart Tap Ready\n" +
                "Wireless WAN Data and Voice\n" +
                "Communications (TC78 only)\n" +
                "Radio Frequency North America: 5G FR1: n2, 5, 7, 12, 13, 14, 25,\n" +
                "Band 26, 29, 38, 41, 48, 66, 71, 77, 78; 4G: B2, 4, 5, 7,\n" +
                "12, 13, 14, 17, 25, 26, 29, 38, 41, 48, 66, 71; 3G:\n" +
                "B2, 4, 5; 2G: 850, 1900; Rest of World: 5G FR1:\n" +
                "n1, 2, 3, 5, 7, 8, 20, 28, 38, 40, 41, 66, 71, 77,\n" +
                "78; 4G: B1, 2, 3, 4, 5, 7, 8, 17, 20, 28, 38 ,39, 40,\n" +
                "Wireless WAN Data and Voice\n" +
                "Communications (TC78 only)\n" +
                "41, 42, 43, 66, 71; 3G: 1, 2, 3, 4, 5, 8; 2G: 850,\n" +
                "900, 1800, 1900; China/Japan: 5G FR1: n1, 3 ,5,\n" +
                "8, 38, 40, 41, 77, 78, 79; 4G: B1, 3, 5, 7, 8, 19,\n" +
                "34, 38, 39, 40, 41, 42; 3G: B1, 5, 8 ,19; 2G: 850,\n" +
                "900, 1800\n" +
                "GPS GPS, GLONASS, Galileo, Beidou, QZSS\n" +
                "Dual-Band GNSS — concurrent L1/G1/E1/B1\n" +
                "(GPS/QZSS, GLO, GAL, BeiDou) +\n" +
                "L5/E5a/BDSB2a (GPS/QZSS, GAL, BeiDou);\n" +
                "a-GPS; supports XTRA\n" +
                "Multimedia Wi-Fi Multimedia™ (WMM) and WMM-PS\n" +
                "including TSPEC\n" +
                "ZEBRA and the stylized Zebra head are trademarks of Zebra Technologies Corp., registered in many jurisdictions worldwide. Android is a\n" +
                "trademark of Google LLC. All other trademarks are the property of their respective owners. ©2025 Zebra Technologies Corp. and/or its\n" +
                "affiliates. 05/18/2023 HTML"
        val chunks = splitter.splitText(text)

//        println("Chunks: $chunks")
        chunks.forEachIndexed { index, chunk ->
            println("Chunks: ======={$index}=======")
            println("Chunks: $chunk ${chunk.length} at index $index")
        }
        assertTrue(chunks.all { it.length <=  (chunkSize+chunkOverlap) }) // 確保最大長度限制
    }
//
//    @Test
//    fun `test empty input`() {
//        val splitter = RecursiveCharacterTextSplitter(chunkSize = 10, chunkOverlap = 2)
//        val text = ""
//        val chunks = splitter.splitText(text)
//
//        println("Chunks: $chunks")
//        assertEquals(listOf(""), chunks) // 空字串應回傳 [""] 而不是空列表
//    }
}