package uet.oop.bomberman.level;

import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.exceptions.LoadLevelException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileLevelLoader extends LevelLoader {

	/**
	 * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đọc được
	 * từ ma trận bản đồ trong tệp cấu hình
	 */
	private static char[][] _map;

	public FileLevelLoader(Board board, int level) throws LoadLevelException {
		super(board, level);
	}

	@Override
	public void loadLevel(int level) {
		ArrayList<String> list = new ArrayList<>();

		try {
			FileReader fileReader = new FileReader("res\\levels\\Level" + level + ".txt");
			BufferedReader br = new BufferedReader(fileReader);
			String s =null;
			while ((s=br.readLine())!=null) {
				list.add(s);
			}
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
		}
		// TODO: c?p nh?t c�c gi� tr? ??c ???c v�o _width, _height, _level, _map
		String[] ar = list.get(0).trim().split(" ");
		_level = Integer.parseInt(ar[0]);
		_height = Integer.parseInt(ar[1]);
		_width = Integer.parseInt(ar[2]);
		_map = new char[_height][_width];
		for (int i = 0; i < _height; i++) {
			for (int j = 0; j < _width; j++) {
				_map[i][j] = list.get(i + 1).charAt(j);
			}
		}
	}

	@Override
	public void createEntities() {
		// TODO: tạo các Entity của màn chơi
		// TODO: sau khi tạo xong, gọi _board.addEntity() để thêm Entity vào game

		// TODO: phần code mẫu ở dưới để hướng dẫn cách thêm các loại Entity vào game
		// TODO: hãy xóa nó khi hoàn thành chức năng load màn chơi từ tệp cấu hình
		// thêm Wall
		for (int y = 0; y < _height; y++) {
			for (int x = 0; x < _width; x++) {
				addEntities(_map[y][x], x, y);
			}
		}
	}

	private void addEntities(char c, int x, int y) {
		int pos = x + y * getWidth();

		switch (c) {
			case '#':
				_board.addEntity(pos, new Wall(x, y, Sprite.wall));
				break;
			case '*':
				_board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new Brick(x, y, Sprite.brick)));
				break;
			case 'x':
				_board.addEntity(pos, new LayeredEntity(x, y,
						new Grass(x, y, Sprite.grass),
						new Portal(x, y, _board, Sprite.portal),
						new Brick(x, y, Sprite.brick)));
				break;
			case 'p':
				_board.addCharacter(new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				Screen.setOffset(0, 0);
				_board.addEntity(pos, new Grass(x, y, Sprite.grass));
				break;
			case '1':
				_board.addCharacter(new Balloon(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntity(pos, new Grass(x, y, Sprite.grass));
				break;
			case '2':
				_board.addCharacter(new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntity(pos, new Grass(x, y, Sprite.grass));
				break;
			case 'b':
				LayeredEntity layer = new LayeredEntity(x, y,
						new Grass(x, y, Sprite.grass),
						new BombItem(x, y, Sprite.powerup_bombs),
						new Brick(x, y, Sprite.brick)
				);
				_board.addEntity(pos, layer);
				break;
			case 'f':
				layer = new LayeredEntity(x, y,
						new Grass(x, y, Sprite.grass),
						new FlameItem(x, y, Sprite.powerup_flames),
						new Brick(x, y, Sprite.brick)
				);
				_board.addEntity(pos, layer);
				break;
			case 's':
				layer = new LayeredEntity(x, y,
						new Grass(x, y, Sprite.grass),
						new SpeedItem(x, y, Sprite.powerup_speed),
						new Brick(x, y, Sprite.brick)
				);
				_board.addEntity(pos, layer);
				break;
			default:
				_board.addEntity(pos, new Grass(x, y, Sprite.grass));
				break;

		}
	}

}
