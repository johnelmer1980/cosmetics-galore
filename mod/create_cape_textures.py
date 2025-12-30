#!/usr/bin/env python3
"""
Generate basic cape textures for the Capes Mod.
Requires: pip install pillow
"""

from PIL import Image, ImageDraw
import os

# Create output directory
output_dir = "src/main/resources/assets/capesmod/textures/entity/capes"
os.makedirs(output_dir, exist_ok=True)

# Cape dimensions (64x32 standard Minecraft cape texture)
WIDTH = 64
HEIGHT = 32

def create_cape(name, base_color, pattern=None):
    """Create a cape texture with optional pattern"""
    img = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Main cape area (10x16 pixels on texture)
    # Fill the cape rectangle
    cape_x = 1
    cape_y = 1
    cape_width = 10
    cape_height = 16

    # Draw main cape
    draw.rectangle(
        [(cape_x, cape_y), (cape_x + cape_width - 1, cape_y + cape_height - 1)],
        fill=base_color
    )

    # Add simple shading
    if pattern == "shaded":
        # Darker on the edges
        edge_color = tuple(max(0, c - 40) for c in base_color[:3]) + (255,)
        draw.line([(cape_x, cape_y), (cape_x, cape_y + cape_height - 1)], fill=edge_color, width=1)
        draw.line([(cape_x + cape_width - 1, cape_y), (cape_x + cape_width - 1, cape_y + cape_height - 1)], fill=edge_color, width=1)

    # Add pattern for rainbow cape
    if pattern == "rainbow":
        colors = [
            (255, 0, 0, 255),    # Red
            (255, 127, 0, 255),  # Orange
            (255, 255, 0, 255),  # Yellow
            (0, 255, 0, 255),    # Green
            (0, 0, 255, 255),    # Blue
            (75, 0, 130, 255),   # Indigo
            (148, 0, 211, 255),  # Violet
        ]
        stripe_height = cape_height / len(colors)
        for i, color in enumerate(colors):
            y_start = cape_y + int(i * stripe_height)
            y_end = cape_y + int((i + 1) * stripe_height)
            draw.rectangle(
                [(cape_x, y_start), (cape_x + cape_width - 1, y_end)],
                fill=color
            )

    filename = os.path.join(output_dir, f"{name}.png")
    img.save(filename)
    print(f"Created {filename}")

# Create capes
print("Generating cape textures...")

create_cape("red_cape", (220, 20, 20, 255), "shaded")
create_cape("blue_cape", (30, 60, 200, 255), "shaded")
create_cape("gold_cape", (255, 215, 0, 255), "shaded")
create_cape("rainbow_cape", (255, 255, 255, 255), "rainbow")

# Additional capes
create_cape("purple_cape", (128, 0, 128, 255), "shaded")
create_cape("green_cape", (0, 180, 0, 255), "shaded")
create_cape("black_cape", (20, 20, 20, 255), "shaded")
create_cape("white_cape", (240, 240, 240, 255), "shaded")

print("\nAll cape textures created successfully!")
print(f"Location: {output_dir}")
print("\nTo use these textures:")
print("1. Make sure you have Python and Pillow installed: pip install pillow")
print("2. Run this script from the mod/ directory: python3 create_cape_textures.py")
print("3. Rebuild the mod: ./gradlew build")
