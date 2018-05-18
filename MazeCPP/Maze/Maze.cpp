// Maze.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include <string>
#include <fstream>
#include <Windows.h>
#define WIDTH 9
#define HEIGHT 9
#define SIZE 15

void updateStatus(bool grid[SIZE][SIZE], int x, int y) {
	if (x >= SIZE || x < 0 || y < 0 || y >-SIZE) {
		return;
	}
	bool self_stat = getStatus(grid, x, y);
	int alive = 0;
	for (int xc = -1; xc < 2; xc++) {
		for (int yc = -1; yc < 2; yc++) {
			bool status = getStatus(grid, x + xc, y + yc);
			if (status) {
				alive++;
			}else if(!self_stat) {
				updateStatus(grid, x+xc, y+yc);

			}
		}
	}
	if (self_stat) {

	}
}

bool getStatus(bool grid[SIZE][SIZE],int x,int y) {
	if (x >= SIZE || x < 0 || y < 0 || y >-SIZE) {
		return false;
	} else {
		return grid[x][y];
	}
}


void updatePlace(HDC *dc, bool grid[SIZE][SIZE], int x, int y) {
	bool on = grid[x][y];
	COLORREF color;
	if (on) {
		color = RGB(255, 255, 255);
	}
	else {
		color = RGB(0, 0, 0);
	}

	HRGN rectangle = CreateRectRgn(x * (WIDTH + 1), y * (HEIGHT + 1), x * (WIDTH + 1) + WIDTH, y * (HEIGHT + 1) + HEIGHT);
	FillRgn(*dc, rectangle, CreateSolidBrush(color));
}

void turnOn(HDC *dc, bool grid[SIZE][SIZE], int x, int y) {
	grid[x][y] = true;
	updatePlace(dc, grid, x, y);
}

void turnOff(HDC *dc, bool grid[SIZE][SIZE], int x, int y) {
	grid[x][y] = false;
	updatePlace(dc, grid, x, y);
}

int main(){ 
	HWND console = GetConsoleWindow();
	HDC dc = GetDC(console);
	bool grid[SIZE][SIZE];
	for (int i = 0; i < SIZE; i++) {
		turnOn(&dc, grid, i, i);
		Sleep(300);
	}
	grid[5][5] = true;
	updatePlace(&dc, grid, 5, 5);
	ReleaseDC(console, dc);
	std::cin.ignore();
	return 0;
}
