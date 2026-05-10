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
- Đạt **Target Score** trước khi hết **Times** để lên Level
- Nhấn **↺ New Game** để chơi lại

## Tính năng
- ✅ 6 loại kẹo với màu sắc và emoji riêng
- ✅ Match 3 (ngang + dọc)
- ✅ Animation hiệu ứng khi match (flash + particles)
- ✅ Pop animation cho viên kẹo trúng
- ✅ Hiệu ứng trượt kẹo
- ✅ HUD: Score, Level, Moves
- ✅ Level up system
- ✅ Try agian 

## Cấu trúc code (Design Patterns)
- **MVC**: Model (Board, Candy, Move, Position, LevelConfig); View (BoardPanel, HUDPanel,                   GameWindow,GameView); Controller (GameEngine, MatchFinder, GameController)
- **Observer**: GameView, HUDPanel, GameWindow, BoardPanel
- **Enum**: CandyColor
  
## Điểm thưởng (cho project)
- 🎯 Extra features: Cascade, Particles, Animations, HUD, Level system (+2pts mỗi cái)
- 🏗️ Design Patterns: Observer, MVC, Enum, Factory (+5pts mỗi cái)
