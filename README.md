# 🍬 Candy Crush - Java Edition

Game lập trình Java với Swing GUI, đồ họa đẹp và animation mượt mà.

## Yêu cầu
- Java JDK 8 trở lên

## Cách chạy

### Windows:
```
run.bat
```

### Linux / Mac:
```bash
chmod +x run.sh
./run.sh
```

### Thủ công:
```bash
mkdir out
javac -d out -sourcepath src src/candycrush/Main.java
java -cp out candycrush.Main
```

## Cách chơi
- **Click** một viên kẹo để chọn (viền sáng trắng)
- **Click** viên kẹo kề cạnh để hoán đổi
- Tạo **3 viên kẹo trùng màu** liên tiếp (ngang hoặc dọc) để ghi điểm
- 4+ viên trùng: **Bonus điểm**
- Đạt **Target Score** trước khi hết **Moves** để lên Level
- Nhấn **↺ New Game** để chơi lại

## Tính năng
- ✅ 6 loại kẹo với màu sắc và emoji riêng
- ✅ Match 3 (ngang + dọc)
- ✅ Cascade (combo tự động)
- ✅ Animation hiệu ứng khi match (flash + particles)
- ✅ Pop animation cho viên kẹo trúng
- ✅ Hover effect
- ✅ HUD: Score, Level, Moves, Progress bar
- ✅ Level up system
- ✅ Game Over / Win dialog

## Cấu trúc code (Design Patterns)
- **MVC**: Model (GameBoard, Candy), View (GamePanel, HudPanel), Controller (GameFrame)
- **Observer**: GameBoard.GameListener interface
- **Enum**: CandyType với properties
- **Factory**: CandyType.random()

## Điểm thưởng (cho project)
- 🎯 Extra features: Cascade, Particles, Animations, HUD, Level system (+2pts mỗi cái)
- 🏗️ Design Patterns: Observer, MVC, Enum, Factory (+5pts mỗi cái)
