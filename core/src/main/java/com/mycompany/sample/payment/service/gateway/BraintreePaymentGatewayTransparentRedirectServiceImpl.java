package com.mycompany.sample.payment.service.gateway;

import com.mycompany.sample.vendor.braintree.service.payment.BraintreePaymentGatewayType;
import com.mycompany.sample.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayConstants;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.AddressDTO;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 5/20/15.
 */
@Service("blBraintreePaymentGatewayTransparentRedirectService")
public class BraintreePaymentGatewayTransparentRedirectServiceImpl implements PaymentGatewayTransparentRedirectService {
  @Resource(name = "blBraintreePaymentGatewayConfiguration")
  protected NullPaymentGatewayConfiguration configuration;

  @Override
  public PaymentResponseDTO createAuthorizeForm(PaymentRequestDTO requestDTO) throws PaymentException {
    return createCommonTRFields(requestDTO);
  }

  @Override
  public PaymentResponseDTO createAuthorizeAndCaptureForm(PaymentRequestDTO requestDTO) throws PaymentException {
    return createCommonTRFields(requestDTO);
  }

  protected PaymentResponseDTO createCommonTRFields(PaymentRequestDTO requestDTO) {
    Assert.isTrue(requestDTO.getTransactionTotal() != null,
      "The Transaction Total on the Payment Request DTO must not be null");
    Assert.isTrue(requestDTO.getOrderId() != null,
      "The Order ID on the Payment Request DTO must not be null");

    //Put The shipping, billing, and transaction amount fields as hidden fields on the form
    //In a real implementation, the gateway will probably provide some API to tokenize this information
    //which you can then put on your form as a secure token. For this sample,
    // we will just place them as plain-text hidden fields on the form
    PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD,
      BraintreePaymentGatewayType.BRAINTREE_GATEWAY)
      .responseMap(NullPaymentGatewayConstants.ORDER_ID, requestDTO.getOrderId())
      .responseMap(NullPaymentGatewayConstants.TRANSACTION_AMT, requestDTO.getTransactionTotal())
      .responseMap(NullPaymentGatewayConstants.TRANSPARENT_REDIRECT_URL,
        configuration.getTransparentRedirectUrl());

    AddressDTO billTo = requestDTO.getBillTo();
    if (billTo != null)  {
      responseDTO.responseMap(NullPaymentGatewayConstants.BILLING_FIRST_NAME, billTo.getAddressFirstName())
        .responseMap(NullPaymentGatewayConstants.BILLING_LAST_NAME, billTo.getAddressLastName())
        .responseMap(NullPaymentGatewayConstants.BILLING_ADDRESS_LINE1, billTo.getAddressLine1())
        .responseMap(NullPaymentGatewayConstants.BILLING_ADDRESS_LINE2, billTo.getAddressLine2())
        .responseMap(NullPaymentGatewayConstants.BILLING_CITY, billTo.getAddressCityLocality())
        .responseMap(NullPaymentGatewayConstants.BILLING_STATE, billTo.getAddressStateRegion())
        .responseMap(NullPaymentGatewayConstants.BILLING_ZIP, billTo.getAddressPostalCode())
        .responseMap(NullPaymentGatewayConstants.BILLING_COUNTRY, billTo.getAddressCountryCode());
    }

    AddressDTO shipTo = requestDTO.getShipTo();
    if (shipTo != null) {
      responseDTO.responseMap(NullPaymentGatewayConstants.SHIPPING_FIRST_NAME, shipTo.getAddressFirstName())
        .responseMap(NullPaymentGatewayConstants.SHIPPING_LAST_NAME, shipTo.getAddressLastName())
        .responseMap(NullPaymentGatewayConstants.SHIPPING_ADDRESS_LINE1, shipTo.getAddressLine1())
        .responseMap(NullPaymentGatewayConstants.SHIPPING_ADDRESS_LINE2, shipTo.getAddressLine2())
        .responseMap(NullPaymentGatewayConstants.SHIPPING_CITY, shipTo.getAddressCityLocality())
        .responseMap(NullPaymentGatewayConstants.SHIPPING_STATE, shipTo.getAddressStateRegion())
        .responseMap(NullPaymentGatewayConstants.SHIPPING_ZIP, shipTo.getAddressPostalCode())
        .responseMap(NullPaymentGatewayConstants.SHIPPING_COUNTRY, shipTo.getAddressCountryCode());
    }

    return responseDTO;

  }
}
