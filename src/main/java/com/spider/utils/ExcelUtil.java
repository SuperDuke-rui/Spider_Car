package com.spider.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * @Author wangrui
 * @Description 生成Excel文件的工具类
 * @date 2022/3/28 17:43
 */
public class ExcelUtil {

    /**
     * 创建excel文档
     * @param list 数据
     * @param keys list中map的key数据集合
     * @param columnNames excel的列名
     * @return excel工作簿
     */
    public static Workbook createWorkBook(List<Map<String, Object>> list, String[] keys, String[] columnNames) {
        //创建excel工作簿
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);  //在内存中只保留100条记录，超过100就将之前的数据存储到磁盘里
        //创建第一个sheet（页），并命名
        Sheet sheet = workbook.createSheet(list.get(0).get("sheetName").toString());
        //手动设置列宽。第一个参数标识要设为第几列；第二个参数标识列的宽度，n为列高的像素数
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth(i, (int)(35.7 * 150));
        }

        //创建第一行
        Row row = sheet.createRow(0);

        //创建两种单元格格式
        CellStyle cellStyle = workbook.createCellStyle();
        CellStyle cellStyle1 = workbook.createCellStyle();

        //创建两种字体
        Font font = workbook.createFont();
        Font font1 = workbook.createFont();

        //创建第一种字体样式（用于列名）
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        //创建第二种字体样式（用于值）
        font1.setFontHeightInPoints((short) 10);
        font1.setColor(IndexedColors.BLACK.getIndex());

        //设置第一种单元格的样式（用于列名）
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());

        //设置第二种单元格的样式（用于值）
        cellStyle1.setFont(font1);
        cellStyle1.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle1.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle1.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);

        //设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cellStyle);
        }

        //设置每行每列的值
        for (int i = 1; i < list.size(); i++) {
            //Row行，Cell方格，Row和Cell都是从0开始计数的
            //创建一行，在页sheet上
            Row row1 = sheet.createRow(i);
            for (int j = 0; j < keys.length; j++) {
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cellStyle1);
            }
        }
        return workbook;
    }

}
