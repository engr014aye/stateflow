import os
import math
from PIL import Image, ImageDraw, ImageFont

OUTPUT_DIR = r"D:\Antigravity\store listing\SlateFlow"
os.makedirs(OUTPUT_DIR, exist_ok=True)

def interpolate_color(c1, c2, factor):
    return tuple(int(a + (b - a) * factor) for a, b in zip(c1, c2))

def draw_radial_glow(draw, center, radius, color, max_alpha):
    cx, cy = center
    for r in range(radius, 0, -3):
        alpha = int(max_alpha * (1.0 - (r / radius) ** 1.5))
        if alpha <= 0:
            continue
        bbox = [cx - r, cy - r, cx + r, cy + r]
        draw.ellipse(bbox, fill=(*color, alpha))

def draw_bezier_curve(draw, points, color, width, steps=100):
    p0, p1, p2, p3 = points
    curve_points = []
    for i in range(steps + 1):
        t = i / steps
        x = (1 - t)**3 * p0[0] + 3 * (1 - t)**2 * t * p1[0] + 3 * (1 - t) * t**2 * p2[0] + t**3 * p3[0]
        y = (1 - t)**3 * p0[1] + 3 * (1 - t)**2 * t * p1[1] + 3 * (1 - t) * t**2 * p2[1] + t**3 * p3[1]
        curve_points.append((x, y))

    for i in range(len(curve_points) - 1):
        draw.line([curve_points[i], curve_points[i+1]], fill=color, width=width)
        draw.ellipse([curve_points[i][0] - width//2, curve_points[i][1] - width//2,
                      curve_points[i][0] + width//2, curve_points[i][1] + width//2], fill=color)

def generate_app_icon():
    width, height = 512, 512
    c1 = (11, 19, 43)    # #0B132B
    c2 = (28, 37, 65)    # #1C2541
    
    img = Image.new("RGBA", (width, height))
    for y in range(height):
        for x in range(width):
            factor = (x + y) / (width + height)
            col = interpolate_color(c1, c2, factor)
            img.putpixel((x, y), (*col, 255))
            
    glow_layer = Image.new("RGBA", (width, height), (0, 0, 0, 0))
    glow_draw = ImageDraw.Draw(glow_layer)
    draw_radial_glow(glow_draw, (256, 230), 220, (0, 245, 212), 40)
    img = Image.alpha_composite(img, glow_layer)
    
    draw = ImageDraw.Draw(img)
    
    # Minimalist Note Slate
    slate_box = [160, 120, 352, 380]
    slate_radius = 28
    draw.rounded_rectangle(slate_box, radius=slate_radius, fill=(20, 30, 52, 230), outline=(255, 255, 255, 255), width=7)
    
    # Slate Text Lines
    draw.rounded_rectangle([195, 175, 265, 187], radius=6, fill=(255, 255, 255, 255))
    draw.rounded_rectangle([195, 215, 315, 227], radius=6, fill=(255, 255, 255, 210))
    draw.rounded_rectangle([195, 255, 275, 267], radius=6, fill=(255, 255, 255, 150))
    
    # Fluid Cyan Flow Wave
    wave_pts = [
        (120, 340),
        (190, 270),
        (310, 380),
        (395, 300)
    ]
    draw_bezier_curve(draw, wave_pts, color=(0, 245, 212, 255), width=16)
    
    # Accent cyan glowing dot
    draw.ellipse([305, 165, 325, 185], fill=(0, 245, 212, 255))

    out_path = os.path.join(OUTPUT_DIR, "app_icon_512x512.png")
    img.save(out_path, "PNG")
    print(f"Generated {out_path}")

def generate_feature_graphic():
    width, height = 1024, 500
    c1 = (7, 11, 18)     # #070B12
    c2 = (15, 23, 42)    # #0F172A
    
    img = Image.new("RGBA", (width, height))
    for y in range(height):
        for x in range(width):
            factor = (x * 0.4 + y * 0.6) / (width * 0.4 + height * 0.6)
            col = interpolate_color(c1, c2, factor)
            img.putpixel((x, y), (*col, 255))
            
    glow_layer = Image.new("RGBA", (width, height), (0, 0, 0, 0))
    glow_draw = ImageDraw.Draw(glow_layer)
    draw_radial_glow(glow_draw, (230, 250), 300, (0, 245, 212), 48)
    draw_radial_glow(glow_draw, (780, 260), 340, (99, 102, 241), 52)
    img = Image.alpha_composite(img, glow_layer)
    
    draw = ImageDraw.Draw(img)
    
    # 2D Logo Mark on Left
    scale = 0.72
    ox, oy = 230, 250
    w_s = int(192 * scale)
    h_s = int(260 * scale)
    
    x1 = ox - w_s // 2
    y1 = oy - h_s // 2
    x2 = ox + w_s // 2
    y2 = oy + h_s // 2
    
    draw.rounded_rectangle([x1, y1, x2, y2], radius=int(28 * scale), fill=(20, 30, 52, 230), outline=(255, 255, 255, 255), width=5)
    
    draw.rounded_rectangle([x1 + int(28*scale), y1 + int(45*scale), x1 + int(98*scale), y1 + int(56*scale)], radius=4, fill=(255, 255, 255, 255))
    draw.rounded_rectangle([x1 + int(28*scale), y1 + int(78*scale), x1 + int(148*scale), y1 + int(89*scale)], radius=4, fill=(255, 255, 255, 210))
    draw.rounded_rectangle([x1 + int(28*scale), y1 + int(111*scale), x1 + int(108*scale), y1 + int(122*scale)], radius=4, fill=(255, 255, 255, 150))
    
    wave_pts = [
        (ox - int(100*scale), oy + int(70*scale)),
        (ox - int(40*scale), oy + int(15*scale)),
        (ox + int(60*scale), oy + int(110*scale)),
        (ox + int(130*scale), oy + int(40*scale))
    ]
    draw_bezier_curve(draw, wave_pts, color=(0, 245, 212, 255), width=int(14 * scale))
    draw.ellipse([x2 - int(45*scale), y1 + int(38*scale), x2 - int(25*scale), y1 + int(58*scale)], fill=(0, 245, 212, 255))
    
    # Typography
    font_large = None
    font_sub = None
    font_badge = None
    
    possible_fonts = [
        r"C:\Windows\Fonts\segoeuib.ttf",
        r"C:\Windows\Fonts\arialbd.ttf"
    ]
    for font_path in possible_fonts:
        if os.path.exists(font_path):
            try:
                font_large = ImageFont.truetype(font_path, 60)
                font_sub = ImageFont.truetype(font_path.replace("bd.ttf", ".ttf").replace("b.ttf", ".ttf"), 24)
                font_badge = ImageFont.truetype(font_path, 16)
                break
            except Exception:
                pass
                
    if not font_large:
        font_large = ImageFont.load_default()
        font_sub = ImageFont.load_default()
        font_badge = ImageFont.load_default()

    tx = 380
    ty = 135
    
    # Title
    draw.text((tx, ty), "SlateFlow", fill=(255, 255, 255, 255), font=font_large)
    
    # Subtitle
    draw.text((tx, ty + 80), "Offline Notes & Tasks", fill=(0, 245, 212, 255), font=font_sub)
    
    # Secondary description
    draw.text((tx, ty + 120), "Private, Fluid & Responsive Productivity", fill=(148, 163, 184, 255), font=font_sub)
    
    # Feature Badges
    badges = ["100% Offline", "Zero Tracking", "Liquid Glass UI"]
    bx = tx
    by = ty + 180
    for badge in badges:
        bbox = draw.textbbox((0, 0), badge, font=font_badge)
        bw = bbox[2] - bbox[0] + 28
        bh = 34
        draw.rounded_rectangle([bx, by, bx + bw, by + bh], radius=17, fill=(255, 255, 255, 22), outline=(255, 255, 255, 45), width=1)
        draw.text((bx + 14, by + 8), badge, fill=(248, 250, 252, 255), font=font_badge)
        bx += bw + 14

    out_path = os.path.join(OUTPUT_DIR, "feature_graphic_1024x500.png")
    img.save(out_path, "PNG")
    print(f"Generated {out_path}")

if __name__ == "__main__":
    generate_app_icon()
    generate_feature_graphic()
