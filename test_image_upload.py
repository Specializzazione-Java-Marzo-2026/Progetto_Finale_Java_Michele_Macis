#!/usr/bin/env python3
import requests
import os
from pathlib import Path
from io import BytesIO
from PIL import Image as PILImage

# Create a test image file
def create_test_image():
    img = PILImage.new('RGB', (100, 100), color='red')
    img_bytes = BytesIO()
    img.save(img_bytes, format='PNG')
    img_bytes.seek(0)
    return img_bytes

# Test configuration
BASE_URL = "http://localhost:8082"
USERNAME = "writer@example.com"
PASSWORD = "password123"

session = requests.Session()

# Step 1: Login
print("Step 1: Logging in...")
login_response = session.post(f"{BASE_URL}/login", data={
    "username": USERNAME,
    "password": PASSWORD
})
print(f"Login status: {login_response.status_code}")

# Step 2: Create test image
print("\nStep 2: Creating test image...")
test_image = create_test_image()

# Step 3: Submit article with image
print("\nStep 3: Submitting article with image...")
article_data = {
    "title": "Test Article with Image",
    "content": "This is a test article to verify image upload functionality",
    "category": {
        "id": "1"  # Tecnologia category
    }
}

files = {
    "file": ("test_image.png", test_image, "image/png")
}

# Note: The form fields depend on the actual form structure
form_data = {
    "title": "Test Article with Image",
    "content": "This is a test article to verify image upload functionality",
    "category": "1"
}

response = session.post(f"{BASE_URL}/article/create", data=form_data, files=files)
print(f"Article creation status: {response.status_code}")
print(f"Response URL: {response.url}")

print("\nTest complete. Check the application logs for image processing details.")
print("Query database with: SELECT id, path, article_id FROM images ORDER BY id DESC LIMIT 1;")
