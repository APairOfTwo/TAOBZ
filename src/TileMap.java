import java.awt.Graphics2D;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * BaseLayer = background -> no collision
 * Layer1 = midground -> collision to not fall
 * Layer2 = spikes on the ground -> death on collision
 * Layer3 = objects in the background or foreground -> no collision
 * 
 */

public class TileMap {

	public Image TileSet = null;
	int MapX;
	int MapY;
	int NTileX, NTileY;
	int Largura = 60;
	int Altura = 50;
	int TilePLinhaTileset;

	int[][] mapBaseLayer;
	int[][] mapLayer1;
	int[][] mapLayer2;
	int[][] mapLayer3;

	public TileMap(Image tileset, int tilestelaX, int tilestelaY) {
		NTileX = tilestelaX;
		NTileY = tilestelaY;
		TileSet = tileset;
		MapX = 0;
		MapY = 0;
		TilePLinhaTileset = TileSet.getWidth(null) >> 4;
	}

	public void OpenMap(String nomemapa) {

		try {
			InputStream In = getClass().getResourceAsStream(nomemapa);

			System.out.println(" In " + In.available());

			DataInputStream data = new DataInputStream(In);

			int Versao = data.readInt(); // lê Versao
			Largura = ReadCInt(data); // lê Largura
			Altura = ReadCInt(data); // lê Largura

			System.out.println(" Largura " + Largura);

			System.out.println(" Altura " + Altura);

			int ltilex = ReadCInt(data);// lê Larg Tile
			int ltiley = ReadCInt(data);// lê Altura Tile

			System.out.println(" ltilex " + ltilex);

			System.out.println(" ltiley " + ltiley);

			byte nome[] = new byte[32];

			data.read(nome, 0, 32); // lê Nome Tilemap
			data.read(nome, 0, 32);

			int numLayers = ReadCInt(data);// lê numero de Layers
			int numTiles = ReadCInt(data);// lê numero de Tiles

			System.out.println(" numLayers " + numLayers);
			System.out.println(" numTiles " + numTiles);

			int BytesPorTiles = ReadCInt(data); // lê numero de bytes por tile;

			System.out.println(" BytesPorTiles " + BytesPorTiles);

			int vago1 = ReadCInt(data); // lê vago;
			int vago2 = ReadCInt(data); // lê vago;

			mapBaseLayer = new int[Altura][Largura];
			mapLayer1 = new int[Altura][Largura];
			mapLayer2 = new int[Altura][Largura];
			mapLayer3 = new int[Altura][Largura];

			if (BytesPorTiles == 1) {
				for (int j = 0; j < Altura; j++) {
					for (int i = 0; i < Largura; i++) {
						int dado;

						int b1 = data.readByte();
						int b2 = data.readByte();

						dado = ((int) b1 & 0x00ff) | (((int) b2 & 0x00ff) << 8);

						mapBaseLayer[j][i] = dado;
					}
				}
				if (numLayers == 2) {
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;

							int b1 = data.readByte();
							int b2 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8);
							
							mapLayer1[j][i] = dado;
						}
					}
				} else if(numLayers == 3) {
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;

							int b1 = data.readByte();
							int b2 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8);
							
							mapLayer1[j][i] = dado;
						}
					}
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;

							int b1 = data.readByte();
							int b2 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8);
							
							mapLayer2[j][i] = dado;
						}
					}
				} else if(numLayers == 4) {
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;

							int b1 = data.readByte();
							int b2 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8);
							
							mapLayer1[j][i] = dado;
						}
					}
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;

							int b1 = data.readByte();
							int b2 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8);
							
							mapLayer2[j][i] = dado;
						}
					}
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;

							int b1 = data.readByte();
							int b2 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8);
							
							mapLayer3[j][i] = dado;
						}
					}
				}
			} else if (BytesPorTiles == 2) {
				for (int j = 0; j < Altura; j++) {
					for (int i = 0; i < Largura; i++) {
						int dado;
						
						int b1 = data.readByte();
						int b2 = data.readByte();
						int b3 = data.readByte();
						int b4 = data.readByte();

						dado = ((int) b1 & 0x00ff) | (((int) b2 & 0x00ff) << 8)
								| (((int) b3 & 0x00ff) << 16)
								| (((int) b4 & 0x00ff) << 24);

						mapBaseLayer[j][i] = dado;
					}
				}
				if (numLayers == 2) {
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;
							
							int b1 = data.readByte();
							int b2 = data.readByte();
							int b3 = data.readByte();
							int b4 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8)
									| (((int) b3 & 0x00ff) << 16)
									| (((int) b4 & 0x00ff) << 24);

							mapLayer1[j][i] = dado;
						}
					}
				} else if(numLayers == 3) {
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;
							
							int b1 = data.readByte();
							int b2 = data.readByte();
							int b3 = data.readByte();
							int b4 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8)
									| (((int) b3 & 0x00ff) << 16)
									| (((int) b4 & 0x00ff) << 24);

							mapLayer1[j][i] = dado;
						}
					}
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;
							
							int b1 = data.readByte();
							int b2 = data.readByte();
							int b3 = data.readByte();
							int b4 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8)
									| (((int) b3 & 0x00ff) << 16)
									| (((int) b4 & 0x00ff) << 24);

							mapLayer2[j][i] = dado;
						}
					}
				} else if(numLayers == 4) {
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;
							
							int b1 = data.readByte();
							int b2 = data.readByte();
							int b3 = data.readByte();
							int b4 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8)
									| (((int) b3 & 0x00ff) << 16)
									| (((int) b4 & 0x00ff) << 24);

							mapLayer1[j][i] = dado;
						}
					}
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;
							
							int b1 = data.readByte();
							int b2 = data.readByte();
							int b3 = data.readByte();
							int b4 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8)
									| (((int) b3 & 0x00ff) << 16)
									| (((int) b4 & 0x00ff) << 24);

							mapLayer2[j][i] = dado;
						}
					}
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;
							
							int b1 = data.readByte();
							int b2 = data.readByte();
							int b3 = data.readByte();
							int b4 = data.readByte();

							dado = ((int) b1 & 0x00ff)
									| (((int) b2 & 0x00ff) << 8)
									| (((int) b3 & 0x00ff) << 16)
									| (((int) b4 & 0x00ff) << 24);

							mapLayer3[j][i] = dado;
						}
					}
				}
			} else {
				for (int j = 0; j < Altura; j++) {
					for (int i = 0; i < Largura; i++) {
						int dado;

						int b1 = data.readByte();

						// System.out.println(" "+b1);

						dado = ((int) b1 & 0x00ff);

						mapBaseLayer[j][i] = dado;
					}
				}
				if (numLayers == 2) {
					for (int j = 0; j < Altura; j++) {
						for (int i = 0; i < Largura; i++) {
							int dado;

							int b1 = data.readByte();

							dado = ((int) b1 & 0x00ff);

							mapLayer1[j][i] = dado;
							mapLayer2[j][i] = dado;
							mapLayer3[j][i] = dado;
						}
					}
				}
			}

			data.close();

			In.close();

		}// fim try
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage() + "  abreaMapaPau!!!");
		}

	}

	public void selfDraws(Graphics2D dbg) {
		int offx = MapX & 0x0f;
		int offy = MapY & 0x0f;
		int somax, somay;
		if (offx > 0) {
			somax = 1;
		} else {
			somax = 0;
		}
		if (offy > 0) {
			somay = 1;
		} else {
			somay = 0;
		}
		for (int j = 0; j < NTileY + somay; j++) {
			for (int i = 0; i < NTileX + somax; i++) {

				int tilex = (mapBaseLayer[j + (MapY >> 4)][i + (MapX >> 4)] % TilePLinhaTileset) << 4;
				int tiley = (mapBaseLayer[j + (MapY >> 4)][i + (MapX >> 4)] / TilePLinhaTileset) << 4;
				dbg.drawImage(TileSet, (i << 4) - offx, (j << 4) - offy,
						(i << 4) + 16 - offx, (j << 4) + 16 - offy, tilex,
						tiley, tilex + 16, tiley + 16, null);

			}
		}
		for (int j = 0; j < NTileY + somay; j++) {
			for (int i = 0; i < NTileX + somax; i++) {

				int tilex = (mapLayer1[j + (MapY >> 4)][i + (MapX >> 4)] % TilePLinhaTileset) << 4;
				int tiley = (mapLayer1[j + (MapY >> 4)][i + (MapX >> 4)] / TilePLinhaTileset) << 4;
				dbg.drawImage(TileSet, (i << 4) - offx, (j << 4) - offy,
						(i << 4) + 16 - offx, (j << 4) + 16 - offy, tilex,
						tiley, tilex + 16, tiley + 16, null);

			}
		}
		for (int j = 0; j < NTileY + somay; j++) {
			for (int i = 0; i < NTileX + somax; i++) {

				int tilex = (mapLayer2[j + (MapY >> 4)][i + (MapX >> 4)] % TilePLinhaTileset) << 4;
				int tiley = (mapLayer2[j + (MapY >> 4)][i + (MapX >> 4)] / TilePLinhaTileset) << 4;
				dbg.drawImage(TileSet, (i << 4) - offx, (j << 4) - offy,
						(i << 4) + 16 - offx, (j << 4) + 16 - offy, tilex,
						tiley, tilex + 16, tiley + 16, null);

			}
		}
		for (int j = 0; j < NTileY + somay; j++) {
			for (int i = 0; i < NTileX + somax; i++) {

				int tilex = (mapLayer3[j + (MapY >> 4)][i + (MapX >> 4)] % TilePLinhaTileset) << 4;
				int tiley = (mapLayer3[j + (MapY >> 4)][i + (MapX >> 4)] / TilePLinhaTileset) << 4;
				dbg.drawImage(TileSet, (i << 4) - offx, (j << 4) - offy,
						(i << 4) + 16 - offx, (j << 4) + 16 - offy, tilex,
						tiley, tilex + 16, tiley + 16, null);

			}
		}
	}

	public void Positions(int x, int y) {
		int X = x >> 4;
		int Y = y >> 4;

		if (X < 0) {
			MapX = 0;
		} else if (X >= (Largura - NTileX)) {
			MapX = ((Largura - NTileX)) << 4;
		} else {
			MapX = x;
		}

		if (Y < 0) {
			MapY = 0;
		} else if (Y >= (Altura - NTileY)) {
			MapY = ((Altura - NTileY)) << 4;
		} else {
			MapY = y;
		}

	}

	public int ReadCInt(DataInputStream data) throws IOException {
		int dado;
		int b1 = data.readByte();
		int b2 = data.readByte();
		int b3 = data.readByte();
		int b4 = data.readByte();

		return dado = ((int) b1 & 0x00ff) | (((int) b2 & 0x00ff) << 8)
				| (((int) b3 & 0x00ff) << 16) | (((int) b4 & 0x00ff) << 24);
	}
}
