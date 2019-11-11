package com.reinkes.codingchallenge.codingchallenge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractTest {

	protected String readFile(String fileName) throws IOException {
		return new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName)));
	}
	
}
