package com.project.cms.user.controller;

import com.project.cms.user.domain.customer.ChangeBalanceForm;
import com.project.cms.user.domain.customer.CustomerDto;
import com.project.cms.user.domain.model.Customer;
import com.project.cms.user.exception.CustomException;
import com.project.cms.user.exception.ErrorCode;
import com.project.cms.user.service.customer.CustomerBalanceService;
import com.project.cms.user.service.customer.CustomerService;
import com.project.domain.config.JwtAuthenticationProvider;
import com.project.domain.domain.common.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;
    private final CustomerBalanceService customerBalanceService;

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
        UserVo vo = provider.getUserVo(token);
        Customer c = customerService.findByIdAndEmail(vo.getId(), vo.getEmail()).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return ResponseEntity.ok(CustomerDto.from(c));
    }

    @PostMapping("/balance")
    public ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                 @RequestBody ChangeBalanceForm form) {
        UserVo vo = provider.getUserVo(token);

        return ResponseEntity.ok(
            customerBalanceService.changeBalance(vo.getId(), form).getCurrentMoney());

    }


}
