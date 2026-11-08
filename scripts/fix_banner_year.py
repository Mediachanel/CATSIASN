from PIL import Image, ImageDraw, ImageFont
import numpy as np

for filename in ["public/img_cpns_hero.jpg", "public/cpns_hero_banner_1794011093778.jpg"]:
    im = Image.open(filename).convert("RGB")
    draw = ImageDraw.Draw(im)

    # 1. FIX THE "4" in "2024" on the left:
    # box_2024 is at x: 440..540, y: 305..340
    # The '4' is roughly at x: 508..540, y: 305..338
    # Background color around '4' is (6, 25, 58)
    bg_color = (6, 25, 58)
    text_color = (236, 196, 108)

    # Let's paint over '2024' cleanly and rewrite '2026' with Segoe UI Bold
    # The whole "2024" is at x: 444..540, y: 308..338
    draw.rectangle([440, 305, 542, 342], fill=bg_color)
    
    font_main = ImageFont.truetype("C:/Windows/Fonts/segoeuib.ttf", 30)
    draw.text((441, 305), "2026", fill=text_color, font=font_main)

    # 2. FIX THE MONITOR "2024"
    # On the monitor: "SELEKSI CPNS 2024"
    # Let's find the exact position of "2024" on the monitor:
    # In box_monitor (x: 780..950, y: 0..70), "2024" is around x: 885..930, y: 15..45
    # Monitor background color is around (48, 94, 110)
    mon_bg = (48, 94, 110)
    mon_text_color = (180, 225, 230)
    
    # Let's clear the '2024' on the monitor and write '2026'
    draw.rectangle([880, 15, 935, 42], fill=mon_bg)
    font_mon = ImageFont.truetype("C:/Windows/Fonts/segoeuib.ttf", 16)
    draw.text((883, 17), "2026", fill=mon_text_color, font=font_mon)

    # Also clean up the shadow text on the left if any ("MENUJU ASN PROFESIONAL" stays, but any 2024 is gone)
    im.save(filename, quality=95)
    print(f"Updated {filename} successfully!")
