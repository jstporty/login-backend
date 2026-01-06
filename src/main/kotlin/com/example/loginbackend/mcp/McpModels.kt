package com.example.loginbackend.mcp

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * MCP 프로토콜 메시지들
 */
data class McpRequest(
    val jsonrpc: String = "2.0",
    val id: String,
    val method: String,
    val params: Map<String, Any>? = null
)

data class McpResponse(
    val jsonrpc: String = "2.0",
    val id: String,
    val result: Any? = null,
    val error: McpError? = null
)

data class McpError(
    val code: Int,
    val message: String,
    val data: Any? = null
)

/**
 * 브라우저 제어 명령
 */
data class BrowserCommand(
    val action: String,  // navigate, click, input, submit, screenshot 등
    val selector: String? = null,
    val value: String? = null,
    val url: String? = null
)

/**
 * 브라우저 실행 결과
 */
data class BrowserResult(
    val success: Boolean,
    val message: String?,
    val screenshot: String? = null,
    val elementText: String? = null
)

