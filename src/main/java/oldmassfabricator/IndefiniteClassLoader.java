package oldmassfabricator;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.lang.reflect.Method;

public class IndefiniteClassLoader
{
	private IndefiniteClassLoader() {}
	private Class<?> loadClass;
	public IndefiniteClassLoader(String className) {
		try {
			loadClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Object getField(String fieldName) {
		try {
			return loadClass.getField(fieldName).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Method getMethod(String methodName, Class<?>... paramVarArgs){
		try {
			return loadClass.getMethod(methodName, paramVarArgs);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		// 上で回収されるとは思うけど保険
		throw new NullPointerException(methodName + "というメソッドは存在しません");
	}

	public Item getItem(String itemName) {
		Object ret = getField(itemName);
		if (ret instanceof Item) {
			return (Item)ret;
		}
		throw new NullPointerException(itemName + "という名前のアイテムは存在しません");
	}

	public Block getBlock(String blockName) {
		Object ret = getField(blockName);
		if (ret instanceof Block) {
			return (Block)ret;
		}
		throw new NullPointerException(blockName + "という名前のブロックは存在しません");
	}

	public Class getLoadClass() {
		return loadClass;
	}

	public Object[] getEnum(String enumName) {
		Object[] enumState = null;
		for (Class cls : loadClass.getDeclaredClasses()) {
			if (! enumName.equals(cls.getSimpleName())) continue;
			enumState = cls.getEnumConstants();
			break;
		}

		// nullチェック
		if (enumState == null) {
			throw new NullPointerException("値がnullです");
		}

		return enumState;
	}

	public Object getEnumField(String enumName, String fieldName) {
		Object enumField = null;
		Object[] enumState = getEnum(enumName);
		for (Object o : enumState) if (((Enum)o).name().equals(fieldName)) enumField = o;

		// nullチェック
		if (enumField == null) {
			throw new NullPointerException("値がnullです");
		}
		return enumField;
	}

	public Class getEnumAsClass(String enumName) {
		Object[] enumState = getEnum(enumName);
		return ((Enum)enumState[0]).getClass();
	}

}
