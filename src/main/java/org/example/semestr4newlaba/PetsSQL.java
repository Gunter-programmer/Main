package org.example.semestr4newlaba;

import java.sql.*;
import java.util.ArrayList;
import java.util.PrimitiveIterator;

import static org.example.semestr4newlaba.ActionSimulation.StopSimulation;
import static org.example.semestr4newlaba.Habitat.*;

public class PetsSQL {
    private static String Password = "123", Login = "postgres", Url = "jdbc:postgresql://localhost:5432/postgres";
    public static void checkTable(Connection connection, Statement statement) throws SQLException {
        ResultSet resultSet = null;
        try{
            DatabaseMetaData metaData = connection.getMetaData();
            resultSet = metaData.getTables(null, null, "objects", null);
            if(!resultSet.next()){
                statement.executeUpdate("CREATE TABLE objects (" +
                        "id INTEGER NOT NULL PRIMARY KEY UNIQUE, " +
                        "type VARCHAR(5) NOT NULL," +
                        "X FLOAT NOT NULL," +
                        "Y FLOAT NOT NULL)"
                );
                System.out.println("Таблица создана");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(resultSet != null){
                resultSet.close();
            }
        }
    }
    public static void loadTableObjectsAll() throws SQLException {
        PreparedStatement preparedStatement = null;
        try(Connection connection = DriverManager.getConnection(Url,Login,Password);
            Statement statement = connection.createStatement()){
            checkTable(connection, statement);
            statement.executeUpdate("DELETE FROM objects");
            if(petslist.isEmpty()) return;
            preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "objects (id, type, X, Y) VALUES (?,?,?,?)");
            for(Pets pets : petslist){
                preparedStatement.setInt(1, pets.PetsId);
                if(pets instanceof Cat){
                    preparedStatement.setString(2, "Cat");
                }
                else preparedStatement.setString(2, "Dog");
                preparedStatement.setDouble(3, pets.x);
                preparedStatement.setDouble(4, pets.y);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(preparedStatement != null) preparedStatement.close();
        }
    }
    public static void loadTableCat() throws SQLException {
        PreparedStatement preparedStatement = null;
        try(Connection connection = DriverManager.getConnection(Url,Login,Password);
            Statement statement = connection.createStatement()){
            checkTable(connection, statement);
            statement.executeUpdate("DELETE FROM objects");
            if(petslist.isEmpty()) return;
            preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "objects (id, type, X, Y) VALUES (?,?,?,?)");
            for(Pets pets : petslist){
                if(pets instanceof Cat){
                    preparedStatement.setInt(1, pets.PetsId);
                    preparedStatement.setString(2, "Cat");
                    preparedStatement.setDouble(3, pets.x);
                    preparedStatement.setDouble(4, pets.y);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(preparedStatement != null) preparedStatement.close();
        }
    }
    public static void loadTableDog() throws SQLException {
        PreparedStatement preparedStatement = null;
        try(Connection connection = DriverManager.getConnection(Url,Login,Password);
            Statement statement = connection.createStatement()){
            checkTable(connection, statement);
            statement.executeUpdate("DELETE FROM objects");
            if(petslist.isEmpty()) return;
            preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "objects (id, type, X, Y) VALUES (?,?,?,?)");
            for(Pets pets : petslist){
                if(pets instanceof Dog){
                    preparedStatement.setInt(1, pets.PetsId);
                    preparedStatement.setString(2, "Dog");
                    preparedStatement.setDouble(3, pets.x);
                    preparedStatement.setDouble(4, pets.y);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(preparedStatement != null) preparedStatement.close();
        }
    }
    public static void readTableAll() throws SQLException {
        ResultSet resultSet = null;
        StopSimulation();
        try(Connection connection = DriverManager.getConnection(Url,Login,Password);
            Statement statement = connection.createStatement()){
            checkTable(connection, statement);
            resultSet = statement.executeQuery("SELECT * FROM objects");
            while(resultSet.next()){
                Pets pets = null;
                String type = resultSet.getString("type");
                int id = resultSet.getInt("id");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                if(type.equals("Cat")){
                    pets = new Cat(x, y, id);
                } else pets = new Dog(x,y,id);
                TreeSet.add(pets.PetsId);
                HashMap.put(pets.PetsId, Long.valueOf(1));
                petslist.add(pets);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(resultSet != null) resultSet.close();
        }
    }
    public static void readTableCat() throws SQLException {
        ResultSet resultSet = null;
        StopSimulation();
        try(Connection connection = DriverManager.getConnection(Url,Login,Password);
            Statement statement = connection.createStatement()){
            checkTable(connection, statement);
            resultSet = statement.executeQuery("SELECT * FROM objects WHERE type='Cat'");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                Pets pets = new Cat(x, y, id);
                TreeSet.add(pets.PetsId);
                HashMap.put(pets.PetsId, Long.valueOf(1));
                petslist.add(pets);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(resultSet != null) resultSet.close();
        }
    }
    public static void readTableDog() throws SQLException {
        ResultSet resultSet = null;
        StopSimulation();
        try(Connection connection = DriverManager.getConnection(Url,Login,Password);
            Statement statement = connection.createStatement()){
            checkTable(connection, statement);
            resultSet = statement.executeQuery("SELECT * FROM objects WHERE type='Dog'");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                Pets pets = new Dog(x, y, id);
                TreeSet.add(pets.PetsId);
                HashMap.put(pets.PetsId, Long.valueOf(1));
                petslist.add(pets);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(resultSet != null) resultSet.close();
        }
    }
}
