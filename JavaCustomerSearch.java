import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class JavaCustomerSearch {

	// 接続情報
	private static final String URL =
		"jdbc:sqlserver://localhost:1433;databaseName=CustomerDB;encrypt=true;trustServerCertificate=true";
	private static final String USER = "sa";
	private static final String PASSWORD = "your_password";

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("===============================");
			System.out.println(" 顧客検索ツール");
			System.out.println("===============================");
			System.out.println("1. 顧客一覧表示");
			System.out.println("2. 顧客ID検索");
			System.out.println("3. 顧客名LIKE検索");
			System.out.println("4. 顧客登録");
			System.out.println("5. 顧客更新");
			System.out.println("6. 顧客削除");
			System.out.println("0. 終了");
			System.out.print("選択してください: ");

			String choice = scanner.nextLine().trim();

			if(choice.isEmpty()){
 				   System.out.println("番号を入力してください");
 				   continue;
			}

			switch (choice) {
				case "1":
					showAllCustomers();
					break;
				case "2":
					searchByCustomerId(scanner);
					break;
				case "3":
					searchByName(scanner);
					break;
				case "4":
					insertCustomer(scanner);
					break;
				case "5":
					updateCustomer(scanner);
					break;
				case "6":
					deleteCustomer(scanner);
					break;
				case "0":
					System.out.println("終了します。");
					scanner.close();
					return;
				default:
					System.out.println("無効な入力です。");
			}


			System.out.println();
		}
	}

	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	private static void showAllCustomers() {
		String sql = "SELECT customer_id, customer_name, email FROM customers ORDER BY customer_id";

		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()) {

			System.out.println("---- 顧客一覧 ----");
			boolean found = false;

			while (rs.next()) {
				found = true;
				System.out.println(
					"ID: " + rs.getInt("customer_id")
						+ ", 名前: " + rs.getString("customer_name")
						+ ", メール: " + rs.getString("email")
				);
			}

			if (!found) {
				System.out.println("データがありません。");
			}

		} catch (SQLException e) {
			System.out.println("一覧表示中にエラーが発生しました。");
				e.printStackTrace();
		}
	}

	private static void searchByCustomerId(Scanner scanner) {
		System.out.print("顧客IDを入力してください: ");
		String input = scanner.nextLine();

		int customerId;
		try {
			customerId = Integer.parseInt(input);
		} catch(NumberFormatException e) {
			System.out.println("顧客IDは数値で入力してください。");
			return;
		}

		String sql = "SELECT customer_id, customer_name, email FROM customers WHERE customer_id = ?";

		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, customerId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					System.out.println("---- 検索結果 ----");
					System.out.println(
						"ID: " + rs.getInt("customer_id")
							+ ", 名前: " + rs.getString("customer_name")
							+ ", メール: " + rs.getString("email")
					);

				} else {
					System.out.println("該当する顧客が見つかりませんでした。");
				}
			}
		} catch (SQLException e) {
			System.out.print("顧客ID検索中にエラーが発生しました。");
			e.printStackTrace();
		}
	}

	private static void searchByName(Scanner scanner) {
		System.out.print("顧客名の一部を入力してください: ");
		String keyword = scanner.nextLine();

		if(keyword == null || keyword.trim().isEmpty()){
   			 System.out.println("検索文字を入力してください");
  			  return;
		}

		String sql = "SELECT customer_id, customer_name, email FROM customers "
			+ "WHERE customer_name LIKE ? ORDER BY customer_id";

		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, "%" + keyword + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				System.out.println("---- 検索結果 ----");
				boolean found = false;

				while (rs.next()) {
					found = true;
					System.out.println(
						"ID: " + rs.getInt("customer_id")
							+ ", 名前: " + rs.getString("customer_name")
							+ ", メール: " + rs.getString("email")
					);
				}

				if (!found) {
					System.out.println("該当する顧客が見つかりませんでした。");
				}
			}
		} catch (SQLException e) {
			System.out.println("顧客名検索中にエラーが発生しました。");
			e.printStackTrace();
		}
	}

	private static void insertCustomer(Scanner scanner) {
		System.out.println("---- 顧客登録 ----");

		int customerId;
		System.out.print("顧客IDを入力してください: ");
		String idInput = scanner.nextLine();

		try {
			customerId = Integer.parseInt(idInput);
		} catch (NumberFormatException e) {
			System.out.println("顧客IDは数値で入力してください。");
			return;
		}

		System.out.print("顧客名を入力してください: ");
		String customerName = scanner.nextLine();

		if (customerName == null || customerName.trim().isEmpty()) {
			System.out.println("顧客名は必須です。");
			return;
		}

		System.out.print("メールアドレスを入力してください: ");
		String email = scanner.nextLine();

		if(email == null || email.trim().isEmpty()){
   			 System.out.println("メールは必須です");
    			return;
		}

		if(!email.contains("@")){
    			System.out.println("正しいメール形式ではありません");
  			  return;
		}

		String checkSql = "SELECT customer_id FROM customers WHERE customer_id = ?";
		String insertSql = "INSERT INTO customers (customer_id, customer_name, email) VALUES (?, ?, ?)";

		try (Connection conn = getConnection()) {

			try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
				checkPstmt.setInt(1, customerId);

				try (ResultSet rs = checkPstmt.executeQuery()) {
					if (rs.next()) {
						System.out.println("その顧客IDはすでに登録されています。");
						return;
					}
				}
			}

			try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
				insertPstmt.setInt(1, customerId);
				insertPstmt.setString(2, customerName);
				insertPstmt.setString(3, email);

				int count = insertPstmt.executeUpdate();

				if (count > 0) {
					System.out.println("顧客を登録しました。");
				} else {
					System.out.println("顧客登録に失敗しました。");
				}
			}

		} catch (SQLException e) {
			System.out.println("顧客登録中にエラーが発生しました。");
			e.printStackTrace();
		}
	}

	private static void updateCustomer(Scanner scanner) {
		System.out.println("---- 顧客更新 ----");

		int customerId;
		System.out.print("更新対象の顧客IDを入力してください: ");
		String idInput = scanner.nextLine();

		try {
			customerId = Integer.parseInt(idInput);
		} catch (NumberFormatException e) {
			System.out.println("顧客IDは数値で入力してください。");
			return;
		}

		String selectSql = "SELECT customer_id, customer_name, email FROM customers WHERE customer_id = ?";
		String updateSql = "UPDATE customers SET customer_name = ?, email = ? WHERE customer_id = ?";

		try (Connection conn = getConnection()) {

			try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
				selectPstmt.setInt(1, customerId);

				try (ResultSet rs = selectPstmt.executeQuery()) {
					if (!rs.next()) {
						System.out.println("該当する顧客が見つかりませんでした。");
						return;
					}

					String currentName = rs.getString("customer_name");
					String currentEmail = rs.getString("email");

					System.out.println("現在の顧客名: " + currentName);
					System.out.println("現在のメールアドレス: " + currentEmail);

					System.out.print("新しい顧客名を入力してください: ");
					String newName = scanner.nextLine();

					if (newName == null || newName.trim().isEmpty()) {
						System.out.println("顧客名は必須です。");
						return;
					}

					if(newName.length() > 50){
 						   System.out.println("名前は50文字以内");
						    return;
					}

					System.out.print("新しいメールアドレスを入力してください: ");
					String newEmail = scanner.nextLine();

					if(newEmail == null || newEmail.trim().isEmpty()){
   						 System.out.println("メールは必須です");
   					 	return;
					}

					if(!newEmail.contains("@")){
  						  System.out.println("正しいメール形式ではありません");
   						 return;
					}

					try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
						updatePstmt.setString(1, newName);
						updatePstmt.setString(2, newEmail);
						updatePstmt.setInt(3, customerId);

						int count = updatePstmt.executeUpdate();

						if (count > 0) {
							System.out.println("顧客情報を更新しました。");
						} else {
							System.out.println("顧客情報の更新に失敗しました。");
						}
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("顧客更新中にエラーが発生しました。");
			System.out.println("詳細：" + e.getMessage());
		}
	}

	private static void deleteCustomer(Scanner scanner) {
		System.out.println("---- 顧客削除 ----");

		int customerId;
		System.out.print("削除対象の顧客IDを入力してください: ");
		String idInput = scanner.nextLine();

		try {
			customerId = Integer.parseInt(idInput);
		} catch (NumberFormatException e) {
			System.out.println("顧客IDは数値で入力してください。");
			return;
		}

		System.out.print("本当に削除しますか？ (y/n): ");
		String confirm = scanner.nextLine().trim();

		if (!confirm.equalsIgnoreCase("y")) {
			System.out.println("削除をキャンセルしました。");
			return;
		}

		String deleteSql = "DELETE FROM customers WHERE customer_id = ?";

		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

			pstmt.setInt(1, customerId);

			int count = pstmt.executeUpdate();

			if (count > 0) {
				System.out.println("顧客情報を削除しました。");
			} else {
				System.out.println("該当する顧客情報が見つかりませんでした。");
			}
		} catch (SQLException e) {
			System.out.println("顧客削除中にエラーが発生しました。");
			System.out.println("詳細：" + e.getMessage());
		}
	}		
}
