package com.motartin.connector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConnectorType {

	SMB("smb"),
	DEFAULT("default");

	private final String name;
}
