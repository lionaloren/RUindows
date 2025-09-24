🖥️ RUindows App
=======================

A simple operating system-like JavaFX application where users can manage shortcuts, open apps, and interact with files. Users can explore desktop-like interactions including Notepad, ChRUme, Trash, and Photo Editor. Users can save and rename files, open multiple instances, and interact with multimedia content.

---

📝 Project Overview
-------------------

This project was developed as part of the **Multimedia Programming Foundation Lab** course.  
The prototype is implemented fully in **JavaFX** with the source code located in the `src` folder.  

This repository contains:

- The **JavaFX source code** (`src/`)  

Main features include:

- Password-protected login  
- Desktop-like home page with shortcut management  
- Multiple instances of Notepad, Text File, ChRUme, and Photo Editor  
- File saving with automatic shortcut creation  
- Multimedia content in ChRUme (video, audio, images)  

---

✨ Features
----------

### 1. Login Page
- Password input field  
- Error message: **"Wrong password!"** for incorrect entries  
- Correct password (`123`) redirects user to **Home Page**  

### 2. Home Page
- Desktop interface with shortcuts  
- Default shortcuts: **Trash**, **Notepad**, **ChRUme**  
- Taskbar menu:
  - **Window** → Log Out, Shutdown  
  - **Notepad** → Opens Notepad app  
- Automatic column adjustment if more than 5 shortcuts  

### 3. Notepad Application
- Multiple Notepad instances supported  
- Text editing and saving via `File > Save`  
- Automatic file naming:
  - Default: `text.txt`  
  - Subsequent files: `textN.txt`  
- Filenames must be **alphanumeric** and **unique**  
- Saved files create new Home Page shortcuts  

### 4. Text File Application
- Opens when clicking a saved text file shortcut  
- Displays filename as window title  
- Loads and allows editing of saved content  
- Saving updates internal program data  
- Reopening reflects latest saved version  

### 5. ChRUme Application
- Mini browser for specific content  
- Multiple instances supported  
- Supported content:
  - Empty page (default)  
  - Unknown domain → **"This site can't be reached"**  
  - **RUtube.net** → Plays `DiamondJack.mp4` (Play/Pause)  
  - **RUtify.net** → Plays `PromQueen.mp3` (Play/Pause, slider non-interactive)  
  - **stockimages.net** → Displays cat images with download option  
- Downloaded images create new Home Page shortcuts  
- Filenames must be **alphanumeric**, **unique**, with `.jpg` extension  

### 6. Photo Editor Application
- Opens when clicking a saved image shortcut  
- Window title shows image filename  
- Zoom and pan supported  
- **Rotate** menu rotates image 90° per click  

---

🛠️ Tech & Tools Used
---------------------
- **JavaFX** → Full GUI and app logic implementation  

---

## 👩‍💻 Contributors
- **Emily Wilkinson**
- **Liona Loren**

---

## 📸 Screenshots
### 1. Login Page
<img src="https://github.com/user-attachments/assets/99fac1bf-9eb7-4d8d-9ee6-f3f3f1b39bb0" height="200" />

### 2. Home Page
<img src="https://github.com/user-attachments/assets/fac345a1-a7c5-4d1b-a2c2-edcad8c090bf" height="200" />
<img src="https://github.com/user-attachments/assets/1a4afe3a-b89b-404c-9629-818b5a6f6aec" height="200" />

### 3. Notepad Application
<p float="left">
  <img src="https://github.com/user-attachments/assets/e0a14e02-6540-42b4-adf7-091eed784016" height="200" />
  <img src="https://github.com/user-attachments/assets/434c941c-9b35-45f0-805d-f379204e4dda" height="200" />
</p>
<p float="left">
  <img src="https://github.com/user-attachments/assets/4770cf0e-1aa5-479a-94e3-0b1b758502ea" height="200" />
  <img src="https://github.com/user-attachments/assets/bc73295f-685c-412a-a11e-c96e0cc4d6f3" height="200" />
</p>

### 4. ChRUme Application
<img src="https://github.com/user-attachments/assets/e67a513b-fc2a-4a45-a295-6322f6875d9c" height="200" />
<img src="https://github.com/user-attachments/assets/88f0c377-6210-4f4b-880a-ee290b42eea9" height="200" />

### 5. RUtube.net
<img src="https://github.com/user-attachments/assets/d9b5c322-0073-4e3a-b7ee-1b49c8571f95" height="200" />

### 6. RUtify.net
<img src="https://github.com/user-attachments/assets/a94faa01-4c68-4fc9-aaa5-1c488e41013c" height="200" />

### 7. stockimages.net
<img src="https://github.com/user-attachments/assets/a85af10b-f498-4e67-823d-1e72a8db5fb6" height="200" />

### 8. Photo Editor Application
<img src="https://github.com/user-attachments/assets/69376128-4203-4451-b25a-92ad9db43881" height="200" />
<img src="https://github.com/user-attachments/assets/81507010-1faf-4f03-a1cf-84d21146cc4b" height="200" />
