package hu.project.groupproject.resourceserver.controllers;

import com.fasterxml.jackson.databind.*;
import hu.project.groupproject.resourceserver.customAuth.*;
import hu.project.groupproject.resourceserver.dtos.*;
import hu.project.groupproject.resourceserver.dtos.En.*;
import hu.project.groupproject.resourceserver.entities.softdeletable.*;
import hu.project.groupproject.resourceserver.services.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.event.annotation.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.server.*;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ItemController.class)

class ItemControllerTest{
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	ItemService itemService;
	
	@BeforeTestClass
	void setMockMvc(){
		this.mockMvc= MockMvcBuilders.standaloneSetup(ItemController.class).defaultRequest(get("/")).build();
	}
	
	@Test
	@WithMockUser
	void shouldGetItemById1() throws Exception {
		// Create the item DTO
		ItemDtoPublicWithImages item = new ItemDtoPublicWithImages("1", "1", "TestItem", "My first test item", "used", "Earth","email@mail.bu", 12345678901L,0L, new String[]{"first", "second"});
		
		// Stub the service call
		when(itemService.getItem("1")).thenReturn(item);
		
		// Perform the request and assert the result
		mockMvc.perform(get("/items/{id}", 1).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.itemId").value(item.itemId())) // Adjust this line to match your JSON structure
				.andExpect(jsonPath("$.name").value(item.name())); // Adjust this line to match your JSON structure
	}
	@Test
	@WithMockUser
	void shouldNotGetItemByNonExistentId() throws Exception {
		
		// Stub the service call
		when(itemService.getItem("nonExistentId")).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"No item found by provided id"));
		
		// Perform the request and assert the result
		mockMvc.perform(get("/items/{id}", "nonExistentId").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	@Test
	@WithMockUser(roles = "USER")
	void shouldGetItemsPage1() throws Exception {
		// Mocking service response
		Set<ItemDtoPublicWithImages> items = new HashSet<>();
		// Add sample items
		items.add(new ItemDtoPublicWithImages("1", "1", "TestItem1", "Description1", "used", "Earth","email@mail.bu", 12345678901L,0L, new String[]{"first", "second"}));
		items.add(new ItemDtoPublicWithImages("2", "1", "TestItem2", "Description2", "new", "Mars","email@mail.bu", 9876543210L,0L, new String[]{"third", "fourth"}));
		when(itemService.getItems(1)).thenReturn(items);
		
		// Perform the request
		mockMvc.perform(get("/items/page/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))); // Assuming you expect 2 items in the response
	}
	
	@Test
	@WithMockCustomUser(id="userId", username = "username", roles = {"USER","ADMIN"})
	void testSaveItem() throws Exception {
		// Mocking service response
		ObjectMapper objectMapper = new ObjectMapper();
		ImageUploadDetailsDto uploadDetailsDto = new ImageUploadDetailsDto("images/someFolder", true);
		ItemDto itemDto = new ItemDto("itemId","userId", "TestItem1", "Description1", "used", "Earth", "email@mail.bu", 12345678901L,0L);
		when(itemService.createItem("userId", itemDto)).thenReturn(uploadDetailsDto);
		
		// Perform the request
		mockMvc.perform(post("/items/new").with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(itemDto)))
//				.andExpect(status().isOk())
				.andExpect(jsonPath("$.url").value("images/someFolder"))
				.andExpect(jsonPath("$.multiple").value(true));
	}
	
	@Test
	@WithMockCustomUser(id="userId", username = "username", roles = {"USER","ADMIN"})
	void testUpdateItem() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		
		// Mocking service response
		ImageUploadDetailsDto uploadDetailsDto = new ImageUploadDetailsDto("images/someFolder", true);
		ItemDto itemDto = new ItemDto("itemId", "userId", "TestItem1", "Description1", "used", "Earth", "email.mail.bu", 12345678901L,0L);
		when(itemService.updateItem("userId","itemId", itemDto)).thenReturn(uploadDetailsDto);
		
		// Perform the request
		mockMvc.perform(put("/items/{itemId}","itemId").with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(itemDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.url").value("images/someFolder"))
				.andExpect(jsonPath("$.multiple").value(true));
	}
	
//	@Test
//	@WithMockUser(setupBefore = userSetup)
//	void testDeleteItem() throws Exception {
//		// Mock the authentication object
//		Authentication authentication = mock(Authentication.class);
//		// Stub the principal
//		MyUser user = new MyUser();
//		user.setId("userId");
//		when(authentication.getPrincipal()).thenReturn(user);
//
//		// Perform the request
//		mockMvc.perform(delete("/items/del/{itemId}", "itemId").principal(authentication))
//				.andExpect(status().isOk());
//
//		// Verify that the service method was called with the correct arguments
//		verify(itemService).deleteItem("userId", "itemId");
//	}

}