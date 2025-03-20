package com.example.mappers;

import com.example.dtos.ContactDto;
import com.example.entity.Contact;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ContactMapper {

    public Contact dtoToEntity(ContactDto contactDto) {
        if (contactDto == null) {
            return null;
        } else {
            return Contact.builder()
                    .uuid(contactDto.getUuid())
                    .info(contactDto.getInfo())
                    .name(contactDto.getName())
                    .externId(contactDto.getExternId())
                    .contactPhoto(contactDto.isContactPhoto())
                    .primePhoto(contactDto.isPrimePhoto())
                    .build();

        }
    }

    public ContactDto entityToDto(Contact contact) {
        if (contact == null) {
            return null;
        } else {
            ContactDto.ContactDtoBuilder contactDtoBuilder = ContactDto.builder();
            contactDtoBuilder.uuid(contact.getUuid());
            contactDtoBuilder.info(contact.getInfo());
            contactDtoBuilder.contactPhoto(contact.isContactPhoto());
            contactDtoBuilder.primePhoto(contact.isPrimePhoto());
            contactDtoBuilder.name(contact.getName());
            contactDtoBuilder.externId(contact.getExternId());
            return contactDtoBuilder.build();
        }
    }

    public Set<ContactDto> entitySetToDtoSet(Set<Contact> set) {
        if (set == null) {
            return null;
        } else {
            Set<ContactDto> set1 = new HashSet<>();
            for (Contact contact : set) {
                set1.add(this.entityToDto(contact));
            }
            return set1;
        }
    }
}
